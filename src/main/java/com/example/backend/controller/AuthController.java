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

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Token nach "Bearer " extrahieren
            String token = authorizationHeader.substring(7);
            try {
                User user = userService.getCurrentUserByToken(token);
                return ResponseEntity.ok(user);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            // Falls der Header nicht das richtige Format hat, gib einen Fehler zurück
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.getUsername()) || userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Username oder E-Mail bereits vergeben");
        }

        userService.registerUser(userDTO);
        return ResponseEntity.ok("Benutzer erfolgreich registriert");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateUser(loginRequest);
        if (token == null) {
            return ResponseEntity.status(401).body("Ungültige Anmeldedaten");
        }
        // Token in JSON-Format zurückgeben
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
