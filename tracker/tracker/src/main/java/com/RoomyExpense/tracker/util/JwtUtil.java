package com.RoomyExpense.tracker.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "yourSecretKey"; // Cambiar por una clave segura
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    // Método para generar un token JWT
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // Agregamos el rol como claim
                .setIssuedAt(new Date()) // Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de expiración
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Algoritmo de firma y clave secreta
                .compact();
    }

    // Método para validar un token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true; // Si no lanza excepciones, el token es válido
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException ex) {
            // Puedes loguear los errores si lo necesitas
            return false; // Token inválido
        }
    }

    // Método para obtener el email del token
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para obtener el rol del token
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
