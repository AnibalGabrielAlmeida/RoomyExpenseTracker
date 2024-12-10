package com.RoomyExpense.tracker.config;

import com.RoomyExpense.tracker.security.JwtAuthenticationFilter;
import com.RoomyExpense.tracker.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Configuring BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        logger.info("AuthenticationManager configured with UserDetailsService and PasswordEncoder.");
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        logger.info("Configuring SecurityFilterChain...");
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Public routes
                        .requestMatchers("/api/user/saveUser").permitAll()
                        .requestMatchers("/api/house/saveHouse").permitAll()
                        .requestMatchers("/api/user/getAll").permitAll()
                        .requestMatchers("/api/house/getAll").permitAll()
                        .requestMatchers("/api/house/**").hasRole("ADMIN")  // Only accessible for ADMIN
                        .requestMatchers("/api/house/delete").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/roomy/**").hasRole("ROOMY")
                        .anyRequest().authenticated()  // Protect other routes
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        logger.info("SecurityFilterChain configured with JwtAuthenticationFilter.");
        return http.build();
    }
}
