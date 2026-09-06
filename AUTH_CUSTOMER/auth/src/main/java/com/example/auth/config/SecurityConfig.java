package com.example.auth.config;

import com.example.auth.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Registration
                .requestMatchers("/auth/register").permitAll()

                // OTP verification
                .requestMatchers("/auth/verify-otp").permitAll()

                // Login
                .requestMatchers("/auth/login").permitAll()
                
                //refresh token
                .requestMatchers("/auth/refresh").permitAll()
                
                //forgot password which leads to reset password
                .requestMatchers("/auth/forgot-password").permitAll()
                
                //reset password
                .requestMatchers("/auth/reset-password").permitAll()

                // Everything else requires JWT
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}