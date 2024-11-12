package com.example.backend.config;

import com.example.backend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Erstellt eine Bean für den JWT-Authentifizierungsfilter und gibt das JwtTokenUtil und UserService an
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        return new JwtAuthenticationFilter(jwtTokenUtil, userService);
    }

    // Konfiguriert die Sicherheitsrichtlinien der Anwendung
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()  // Erzwingt HTTPS für alle Anfragen
                )
                .csrf(AbstractHttpConfigurer::disable)  // Deaktiviert CSRF-Schutz (abhängig vom Bedarf)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll()  // Erlaubt Anfragen an /api/** ohne Authentifizierung
                        .anyRequest().authenticated()  // Erfordert Authentifizierung für alle anderen Anfragen
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Legt fest, dass die Anwendung stateless ist (JWT-basiert)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Fügt den JWT-Authentifizierungsfilter vor dem Standard-Authentifizierungsfilter hinzu

        return http.build();
    }

    // Konfiguriert den `AuthenticationManager`, der zur Authentifizierung verwendet wird
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Konfiguriert den Passwort-Encoder als `BCryptPasswordEncoder` für sichere Passwortspeicherung
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
