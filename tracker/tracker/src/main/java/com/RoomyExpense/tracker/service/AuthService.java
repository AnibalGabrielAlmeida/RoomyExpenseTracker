package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a los usuarios

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas

    // Método para autenticar al usuario
    public String authenticate(String email, String password) {
        // Buscar al usuario por su email
        User user = userRepository.findByEmail(email)
                .orElse(null);  // Si no existe, devolvemos null

        // Verificar si el usuario existe y si la contraseña es correcta
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user.getRole().name(); // Si la autenticación es exitosa, devolvemos el rol
        }

        return null; // Si las credenciales son incorrectas
    }
}
