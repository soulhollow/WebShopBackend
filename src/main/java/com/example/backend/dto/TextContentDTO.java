package com.example.backend.dto;

public class TextContentDTO {
    private String key;
    private String content;

    public TextContentDTO(String key, String content) {
        this.key = key;
        this.content = content;
    }

    // Getters and Setters
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
