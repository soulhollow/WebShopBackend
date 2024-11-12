package com.example.backend.config;

import com.example.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    // Konstruktor zur Initialisierung des JwtTokenUtil und UserService
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    // Diese Methode führt den Filterprozess durch
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Extrahiert den Autorisierungs-Header aus der Anfrage
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // Prüft, ob der Header vorhanden ist und mit "Bearer " beginnt
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Entfernt das "Bearer "-Präfix, um das JWT zu erhalten
            try {
                email = jwtTokenUtil.extractEmail(jwt); // Extrahiert die E-Mail aus dem JWT
            } catch (Exception e) {
                // Fehler beim Parsen des JWT - Loggt eine Warnung und setzt die Authentifizierung nicht fort
                logger.warn("Ungültiger JWT Token: " + e.getMessage());
            }
        }

        // Prüft, ob die E-Mail im JWT vorhanden und der Nutzer nicht bereits authentifiziert ist
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(email); // Lädt die Benutzerdetails

            // Validiert das Token anhand der Benutzerdetails
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // Erstellt ein Authentication-Token für den Benutzer
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Fügt zusätzliche Details zur Authentifizierung hinzu
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Setzt die Authentifizierung im SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Setzt die Filterkette fort, unabhängig von der Authentifizierung
        chain.doFilter(request, response);
    }
}
