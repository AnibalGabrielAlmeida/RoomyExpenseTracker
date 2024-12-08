package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.AuthDTO;
import com.RoomyExpense.tracker.service.AuthService;
import com.RoomyExpense.tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService; // Servicio de autenticación

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para manejar el JWT

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        // Intentamos autenticar al usuario usando el servicio
        String role = authService.authenticate(authDTO.getEmail(), authDTO.getPassword());

        if (role != null) {
            // Si la autenticación es exitosa, generamos el token JWT
            String token = jwtUtil.generateToken(authDTO.getEmail(), role);

            // Preparamos la respuesta con el token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response); // Devolvemos el token en la respuesta
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas"); // Si falla
        }
    }
}
