package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.repository.UserRepository;
import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
