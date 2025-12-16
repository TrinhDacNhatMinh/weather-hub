package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.UserMapper;
import com.nhom.weather_hub.projection.UserWithStationCount;
import com.nhom.weather_hub.repository.UserRepository;
import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        // Convert user's role into a GrantedAuthority list
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole().getName().toString()));

        // Return a UserDetails object recognized by Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),         // username
                user.getPassword(),         // encoded password
                user.getActive(),            // enabled status
                true,                       // accountNonExpired
                true,                       // credentialsNonExpired
                true,                       // accountNonLocked
                authorities                 // user's authorities (roles)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserWithStationCount> userPage = userRepository.findUserWithStationCount(pageable);
        List<UserResponse> content = userPage.getContent()
                .stream()
                .map(p -> {
                    UserResponse response = new UserResponse();
                    response.setId(p.getId());
                    response.setName(p.getName());
                    response.setUsername(p.getUsername());
                    response.setEmail(p.getEmail());
                    response.setActive(p.getActive());
                    response.setStationCount(p.getStationCount());
                    return response;
                })
                .toList();
        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    @Override
    @Transactional
    public UserResponse lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (user.getActive()) {
            user.setActive(false);
            userRepository.save(user);
        }

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (!user.getActive()) {
            user.setActive(true);
            userRepository.save(user);
        }

        return userMapper.toResponse(user);
    }

}
