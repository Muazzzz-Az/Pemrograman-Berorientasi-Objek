package com.safetrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Menyediakan implementasi BCrypt (Hashing satu arah) ke Spring Container
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Mengatur Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Matikan CSRF untuk fase development REST API
                .cors(cors -> cors.configure(http)) // Integrasi dengan WebConfig CORS sebelumnya
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Buka semua endpoint sementara agar React tidak diblokir
                );
        return http.build();
    }
}