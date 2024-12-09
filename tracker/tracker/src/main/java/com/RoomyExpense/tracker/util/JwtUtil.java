package com.RoomyExpense.tracker.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // Generar un token con email y rol
    public String generateToken(String email, String role) {
        // Generar la clave de firma con SECRET_KEY
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        // Generar el token
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Validar un token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractEmail(token);  // Extraemos el email del token

        // Verificamos si el email del token coincide con el de los detalles del usuario
        if (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
            return true;  // El token es válido y no está expirado
        }
        return false;  // El token es inválido o el usuario no coincide
    }

    // Extraer el email del token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer el rol del token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extraer cualquier información del token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    // Método corregido
    private boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);  // Extraemos todos los claims del token
        Date expirationDate = claims.getExpiration();  // Obtenemos la fecha de expiración
        return expirationDate.before(new Date());  // Verificamos si la fecha de expiración es anterior a la fecha actual
    }

}

