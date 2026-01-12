package com.nhom.weather_hub.security;

import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;
    private final @Lazy UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC
                        // =========================
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/ws/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/verify",
                                "/api/auth/refresh-token"
                        ).permitAll()

                        // =========================
                        // ROLE / AUTHORITY (ADMIN)
                        // =========================
                        .requestMatchers(HttpMethod.GET,
                                "/api/stations/api-key/**",
                                "/api/stations/all",
                                "/api/stations/users/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/api/stations",
                                "/api/stations/batch"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/stations/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/users"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/users/*/lock",
                                "/api/users/*/unlock"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/weather-data/station")
                        .hasRole("ADMIN")

                        // =========================
                        // AUTHENTICATED
                        // =========================
                        .requestMatchers(
                                "/api/auth/logout",
                                "/api/auth/change-password"
                        ).authenticated()

                        .requestMatchers("/api/alerts/**").authenticated()

                        .requestMatchers(HttpMethod.GET,
                                "/api/stations/user/me/stations",
                                "/api/stations/public",
                                "/api/stations/map",
                                "/api/stations/*",
                                "/api/stations/*/threshold"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT,
                                "/api/stations/attach",
                                "/api/stations/*",
                                "/api/stations/*/public"
                        ).authenticated()

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/stations/*/user"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT,
                                "/api/thresholds/*"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT,
                                "/api/users/*"
                        ).authenticated()

                        .requestMatchers(HttpMethod.GET,
                                "/api/weather-data")
                        .authenticated()

                        // =========================
                        // FALLBACK
                        // =========================
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
