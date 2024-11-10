package com.example.backend.service;

import com.example.backend.dto.TextContentDTO;
import com.example.backend.model.TextContent;
import com.example.backend.repository.TextContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TextContentService {

    @Autowired
    private TextContentRepository textContentRepository;

    public TextContentDTO getTextContentByKey(String key) {
        Optional<TextContent> textContent = textContentRepository.findByKey(key);
        if (textContent.isPresent()) {
            TextContent content = textContent.get();
            return new TextContentDTO(content.getKey(), content.getContent());
        } else {
            throw new RuntimeException("Content not found for key: " + key);
        }
    }
}
