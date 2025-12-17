package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.domain.enums.RoleName;
import com.nhom.weather_hub.domain.policy.LoginPolicy;
import com.nhom.weather_hub.dto.request.ChangePasswordRequest;
import com.nhom.weather_hub.dto.request.LoginRequest;
import com.nhom.weather_hub.dto.request.RefreshTokenRequest;
import com.nhom.weather_hub.dto.request.RegisterRequest;
import com.nhom.weather_hub.dto.response.AuthResponse;
import com.nhom.weather_hub.dto.response.LoginResponse;
import com.nhom.weather_hub.dto.response.RegisterResponse;
import com.nhom.weather_hub.dto.response.VerifyResponse;
import com.nhom.weather_hub.entity.RefreshToken;
import com.nhom.weather_hub.entity.Role;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.entity.VerificationToken;
import com.nhom.weather_hub.exception.business.EmailAlreadyExistsException;
import com.nhom.weather_hub.exception.business.InvalidPasswordException;
import com.nhom.weather_hub.exception.business.UserNotActiveException;
import com.nhom.weather_hub.exception.business.UsernameAlreadyExistsException;
import com.nhom.weather_hub.repository.RefreshTokenRepository;
import com.nhom.weather_hub.repository.UserRepository;
import com.nhom.weather_hub.repository.VerificationTokenRepository;
import com.nhom.weather_hub.security.JwtUtil;
import com.nhom.weather_hub.service.AuthService;
import com.nhom.weather_hub.service.MailService;
import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final UserService userService;
    public static final Role DEFAULT_ROLE = Role.builder()
            .id(2L)
            .name(RoleName.ROLE_USER)
            .build();

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!user.getActive()) {
            throw new DisabledException("Account is not active");
        }
        LoginPolicy.validate(user, request.getAccessChannel());
        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        RefreshToken tokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new LoginResponse(accessToken, refreshToken, user.getName(), user.getEmail());
    }

    @Override
    @Transactional
    public void logout(String authHeader) {
        String token = extractToken(authHeader);
        refreshTokenRepository.deleteByToken(token);
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new BadCredentialsException("Refresh token expired");
        }
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String username = jwtUtil.getUsernameFromToken(request.getRefreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        RefreshToken newTokenEntity = RefreshToken.builder()
                .user(user)
                .token(newRefreshToken)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        refreshTokenRepository.save(newTokenEntity);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .active(false)
                .role(DEFAULT_ROLE)
                .build();
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiryDate(Instant.now().plusSeconds(86400))
                .user(user)
                .build();
        verificationTokenRepository.save(verificationToken);
        mailService.sendVerificationEmail(user.getEmail(), token);

        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getActive(),
                "Register successfully. Please check your email to verify."
        );
    }

    @Override
    @Transactional
    public VerifyResponse verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verify token"));
        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        return new VerifyResponse(
                user.getEmail(),
                true,
                Instant.now(),
                "Account verified successfully"
        );
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = userService.getCurrentUser();

        if (!user.getActive()) {
            throw new UserNotActiveException();
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
