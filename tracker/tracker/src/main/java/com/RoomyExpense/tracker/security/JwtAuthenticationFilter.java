package com.RoomyExpense.tracker.security;

import com.RoomyExpense.tracker.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Ignorar rutas permitidas
        if (requestURI.startsWith("/api/auth/")
                || requestURI.startsWith("/api/user/")
                || requestURI.startsWith("/api/house/")) {
            logger.info("Skipping JWT filter for permitted route: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            email = jwtUtil.extractEmail(token);
            logger.info("Received Authorization header. Extracted token for email: {}", email);
        } else {
            logger.warn("No Authorization header or Bearer token found in request.");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Extracting role from token for email: {}", email);
            String role = jwtUtil.extractRole(token);
            logger.info("Role extracted: {}", role);

            if (jwtUtil.validateToken(token, email)) {
                logger.info("Token validated successfully for email: {}", email);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );
                logger.info("AuthenticationToken created with authorities: {}", authenticationToken.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Authentication set in SecurityContextHolder for email: {}", email);
            } else {
                logger.warn("Token validation failed for email: {}", email);
            }
        }

        filterChain.doFilter(request, response);
    }

}