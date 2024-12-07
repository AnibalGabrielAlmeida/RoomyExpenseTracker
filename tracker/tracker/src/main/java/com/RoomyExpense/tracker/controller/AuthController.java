package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.AuthDTO;
import com.RoomyExpense.tracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService; // Servicio de autenticación

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        // Intentamos autenticar al usuario usando el servicio
        boolean authenticated = authService.authenticate(authDTO.getEmail(), authDTO.getPassword());

        if (authenticated) {
            return ResponseEntity.ok("Login exitoso"); // Si la autenticación es exitosa
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas"); // Si falla
        }
    }
}
