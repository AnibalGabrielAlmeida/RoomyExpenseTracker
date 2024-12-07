package com.RoomyExpense.tracker.config;

import com.RoomyExpense.tracker.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Bean para PasswordEncoder (utilizando BCrypt para cifrado de contraseñas)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para AuthenticationManager (configuración simplificada en Spring Security 6.1)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class); // Usamos el AuthenticationManager compartido por HttpSecurity
    }

    // Bean para UserDetailsService, que obtiene los detalles del usuario desde la base de datos
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // Implementación personalizada del UserDetailsService
    }

    // Configuración de seguridad web
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> requests
                        .requestMatchers("/api/auth/**").permitAll() // Permitimos acceso libre a los endpoints de autenticación
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Solo los usuarios con rol ADMIN pueden acceder a esta ruta
                        .requestMatchers("/roomy/**").hasRole("ROOMY") // Solo los usuarios con rol ROOMY pueden acceder a esta ruta
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .formLogin().disable() // Deshabilitamos el formulario de login (ya que usaremos JWT o Basic Auth)
                .httpBasic().disable(); // Deshabilitamos HTTP Basic si no es necesario

        return http.build();
    }
}
