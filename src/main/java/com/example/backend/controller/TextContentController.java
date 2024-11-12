package com.example.backend.controller;

import com.example.backend.dto.TextContentDTO;
import com.example.backend.service.TextContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/text-content")
public class TextContentController {

    @Autowired
    private TextContentService textContentService;

    // Endpunkt zum Abrufen von Textinhalten basierend auf einem Schlüssel
    @GetMapping("/{key}")
    public TextContentDTO getTextContent(@PathVariable String key) {
        return textContentService.getTextContentByKey(key);
    }
}
