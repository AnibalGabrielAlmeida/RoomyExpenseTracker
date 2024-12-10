package com.RoomyExpense.tracker.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private SecretKey secretKey;
    private final long EXPIRATION_TIME = 3600000L;

    public JwtUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        logger.info("JwtUtil initialized with HS256 secret key.");
    }

    public String generateToken(String email, String role) {
        logger.info("Generating token for email: {} and role: {}", email, role);
        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
        logger.info("Token generated successfully for email: {}", email);
        return token;
    }


    public boolean validateToken(String token, String email) {
        logger.info("Validating token for email: {}", email);
        String tokenEmail = extractEmail(token);
        boolean valid = tokenEmail.equals(email) && !isTokenExpired(token);
        logger.info("Token validation result for email {}: {}", email, valid);
        return valid;
    }

    public String extractEmail(String token) {
        logger.debug("Extracting email from token.");
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        logger.debug("Extracting role from token.");
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        logger.info("Role extracted from token: {}", role);
        return role;
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        logger.debug("Claims extracted: {}", claims);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.debug("Extracting all claims from token.");
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expirationDate = claims.getExpiration();
        boolean expired = expirationDate.before(new Date());
        logger.info("Token expiration check: {}", expired);
        return expired;
    }
}
