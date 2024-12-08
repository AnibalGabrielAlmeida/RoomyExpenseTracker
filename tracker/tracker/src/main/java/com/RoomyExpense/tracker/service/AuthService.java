package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager; // Autenticación usando el manager

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Codificador de contraseñas

    // Método para autenticar al usuario
    public String authenticate(String email, String password) {
        try {
            // Existe el usuario?
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

            // Comprobamos si la contraseña ingresada coincide con la almacenada
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Si las credenciales son correctas, autenticamos al usuario
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

                // Almacenamos la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Obtenemos el rol del usuario autenticado
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                String role = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("USER"); // Si no tiene rol asignado, se asigna un rol por defecto

                return role; // Retornamos el rol
            }
        } catch (Exception e) {
            // En caso de error (usuario no encontrado o contraseña incorrecta)
            return null;
        }

        return null; // Si las credenciales no son válidas
    }
}
