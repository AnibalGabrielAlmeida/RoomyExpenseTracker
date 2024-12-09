package com.RoomyExpense.tracker.config;

import com.RoomyExpense.tracker.security.JwtAuthenticationFilter;
import com.RoomyExpense.tracker.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Bean para PasswordEncoder (utilizando BCrypt para cifrado de contraseñas)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para AuthenticationManager (configuración de Spring Security 6.x)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Configuramos el UserDetailsService y el PasswordEncoder
        return authenticationManagerBuilder.build();
    }

    // Configuración de seguridad web jwt
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Deshabilitamos CSRF para las APIs REST
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Permite acceso sin autenticación a /api/auth/**
                        .requestMatchers("/api/user/saveUser").permitAll()  // Permite creación de usuarios sin autenticación
                        .requestMatchers("/api/house/saveHouse").permitAll()  // Permite creación de casas sin autenticación
                        .requestMatchers("/api/user/getAll").permitAll()  // Permite obtener todos los usuarios sin autenticación
                        .requestMatchers("/api/house/getAll").permitAll()  // Permite obtener todas las casas sin autenticación
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Protege los endpoints para admins
                        .requestMatchers("/roomy/**").hasRole("ROOMY")  // Protege los endpoints para roomies
                        .anyRequest().authenticated()  // Requiere autenticación para cualquier otro endpoint
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);  // Registra el filtro JWT en la cadena de seguridad


        return http.build();
    }



}
