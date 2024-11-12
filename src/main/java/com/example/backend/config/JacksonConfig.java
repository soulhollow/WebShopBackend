package com.example.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Markiert die Klasse als Konfigurationsklasse für Spring
@Configuration
public class JacksonConfig {

    // Definiert eine Bean für die Konfiguration von Jackson
    @Bean
    public ObjectMapper objectMapper() {
        // Erstellt eine Instanz des ObjectMappers
        ObjectMapper mapper = new ObjectMapper();

        // Erstellt ein Hibernate-Modul für Jackson, um Hibernate-spezifische Typen zu unterstützen
        Hibernate5JakartaModule hibernate5Module = new Hibernate5JakartaModule();

        // Konfiguriert das Modul so, dass es Lazy-Loading-Properties von Hibernate ignoriert
        hibernate5Module.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, false);

        // Registriert das Hibernate-Modul im ObjectMapper
        mapper.registerModule(hibernate5Module);

        return mapper;
    }
}
