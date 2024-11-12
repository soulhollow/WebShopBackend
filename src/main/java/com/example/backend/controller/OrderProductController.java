package com.example.backend.controller;

import com.example.backend.config.JwtTokenUtil;
import com.example.backend.dto.OrderProductDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.OrderProductService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orderproducts")
public class OrderProductController {

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    // Endpunkt für das Abrufen von Bestellprodukten basierend auf dem Benutzernamen des authentifizierten Nutzers
    @PermitAll
    @GetMapping("/user")
    public List<OrderProductDTO> getOrderProductsByUsername(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("Authorization Header: " + authorizationHeader);

        // Überprüft, ob der Authorization-Header vorhanden ist und das "Bearer"-Präfix enthält
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        // Extrahiert das Token aus dem Authorization-Header
        String token = authorizationHeader.substring(7); // Entfernt das "Bearer " Präfix
        String email = jwtTokenUtil.getEmailFromToken(token); // Extrahiert die E-Mail-Adresse aus dem Token
        System.out.println("email: " + email);

        // Sucht den Benutzer basierend auf der extrahierten E-Mail-Adresse
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        } else {
            // Ruft die Bestellprodukte für den Benutzer ab und gibt sie zurück
            return orderProductService.getOrderProductsByUsername(user.get().getUsername());
        }
    }
}
