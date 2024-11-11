package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "text_content") // Tabellenname geändert für bessere Konvention
public class TextContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`", unique = true) // Backticks hinzugefügt, um das reservierte Schlüsselwort zu umgehen
    private String key;

    @Lob // Annotation hinzufügen, um große Texte zu unterstützen
    @Column(columnDefinition = "TEXT")
    private String content;

    // Getters und Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
