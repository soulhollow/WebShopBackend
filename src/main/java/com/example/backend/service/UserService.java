package com.example.backend.service;

import com.example.backend.config.JwtTokenUtil;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Für die Passwortverschlüsselung

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Registriert einen neuen Benutzer mit verschlüsseltem Passwort
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Verschlüsselung
        user.setRole("USER");
        return userRepository.save(user); // Speichert den Benutzer in der Datenbank
    }

    // Lädt Benutzerinformationen über die E-Mail-Adresse (wird für die Authentifizierung verwendet)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // Findet einen Benutzer basierend auf dem Benutzernamen
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Holt den Benutzer basierend auf dem Benutzernamen innerhalb einer Transaktion
    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Überprüft, ob der Benutzername bereits vergeben ist
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Überprüft, ob die E-Mail-Adresse bereits vergeben ist
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Sucht einen Benutzer basierend auf der ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Holt den aktuell authentifizierten Benutzer basierend auf dem JWT-Token
    public User getCurrentUserByToken(String authorizationHeader) {
        String email = jwtTokenUtil.extractEmail(authorizationHeader); // Extrahiert die E-Mail aus dem JWT
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Authentifiziert den Benutzer und generiert ein JWT-Token, wenn die Anmeldedaten korrekt sind
    public String authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return jwtTokenUtil.generateToken(user); // JWT-Token generieren und zurückgeben
            }
        }
        return null; // Ungültige Anmeldedaten
    }
}
