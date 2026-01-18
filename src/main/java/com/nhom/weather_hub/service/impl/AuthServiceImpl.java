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
import com.nhom.weather_hub.exception.business.*;
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

    @Value("${jwt.absolute-session-expiration}")
    private long absoluteSessionExpiration;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .password(encodedPassword)
                .email(request.email())
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
                .orElseThrow(() -> new VerifyTokenException("Invalid verify token"));
        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new VerifyTokenException("Verify token expired");
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
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        if (!user.getActive()) {
            throw new AccountNotActiveException();
        }
        LoginPolicy.validate(user, request.accessChannel());
        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        Instant absoluteExpiryDate = Instant.now().plusMillis(absoluteSessionExpiration);
        RefreshToken tokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .absoluteExpiryDate(absoluteExpiryDate)
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new LoginResponse(accessToken, refreshToken, user.getName(), user.getEmail());
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RefreshTokenException("Refresh token expired");
        }
        
        // Check absolute session expiration
        if (tokenEntity.getAbsoluteExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RefreshTokenException("Session expired. Please login again");
        }
        
        if (!jwtUtil.validateToken(request.refreshToken())) {
            throw new RefreshTokenException("Invalid refresh token");
        }

        String username = jwtUtil.getUsernameFromToken(request.refreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RefreshTokenException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        // Preserve the original absolute expiry date
        RefreshToken newTokenEntity = RefreshToken.builder()
                .user(user)
                .token(newRefreshToken)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .absoluteExpiryDate(tokenEntity.getAbsoluteExpiryDate())
                .build();
        refreshTokenRepository.save(newTokenEntity);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String authHeader) {
        String token = extractToken(authHeader);
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = userService.getCurrentUser();

        if (!user.getActive()) {
            throw new AccountNotActiveException();
        }

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }

}
