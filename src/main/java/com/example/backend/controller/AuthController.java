package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpunkt zum Abrufen des aktuell authentifizierten Benutzers
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrahiert das JWT aus dem "Authorization"-Header
            String token = authorizationHeader.substring(7);
            try {
                User user = userService.getCurrentUserByToken(token); // Lädt den aktuellen Benutzer basierend auf dem Token
                return ResponseEntity.ok(user);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build(); // Gibt 404 zurück, wenn kein Benutzer gefunden wird
            }
        } else {
            // Gibt 401 zurück, wenn der Header fehlt oder ein ungültiges Format hat
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Endpunkt zur Registrierung eines neuen Benutzers
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        // Überprüft, ob der Benutzername oder die E-Mail-Adresse bereits vergeben sind
        if (userService.existsByUsername(userDTO.getUsername()) || userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Username oder E-Mail bereits vergeben");
        }

        userService.registerUser(userDTO); // Registriert den neuen Benutzer
        return ResponseEntity.ok("Benutzer erfolgreich registriert"); // Gibt eine Erfolgsmeldung zurück
    }

    // Endpunkt zur Authentifizierung eines Benutzers (Login)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateUser(loginRequest); // Authentifiziert den Benutzer und generiert ein JWT
        if (token == null) {
            return ResponseEntity.status(401).body("Ungültige Anmeldedaten"); // Gibt 401 zurück bei ungültigen Anmeldedaten
        }

        // Erstellt die Antwort mit dem JWT als JSON-Objekt
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response); // Gibt das Token im JSON-Format zurück
    }
}
