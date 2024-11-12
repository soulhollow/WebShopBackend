package com.example.backend.controller;

import com.example.backend.model.ContactMessage;
import com.example.backend.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    // Endpunkt zum Speichern einer Kontaktanfrage
    @PostMapping("/send")
    public ResponseEntity<ContactMessage> saveContactMessage(@RequestBody ContactMessage contactMessage) {
        // Speichert die Kontaktanfrage und gibt sie als Antwort zurück
        ContactMessage savedMessage = contactMessageService.saveContactMessage(contactMessage);
        return ResponseEntity.ok(savedMessage); // Gibt die gespeicherte Nachricht im JSON-Format zurück
    }
}
