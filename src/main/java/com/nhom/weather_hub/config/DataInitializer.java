package com.nhom.weather_hub.config;

import com.nhom.weather_hub.entity.Role;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.repository.RoleRepository;
import com.nhom.weather_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, Role.RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(null, Role.RoleName.ROLE_USER));
        }

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN).orElseThrow();
            User admin = User.builder()
                    .name("Admin")
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .email("admin@example.com")
                    .active(true)
                    .role(adminRole)
                    .build();
            userRepository.save(admin);
        }
    }
}
