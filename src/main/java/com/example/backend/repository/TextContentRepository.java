package com.example.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.TextContent;
import java.util.Optional;

public interface TextContentRepository extends JpaRepository<TextContent, Long> {
    Optional<TextContent> findByKey(String key);
}
