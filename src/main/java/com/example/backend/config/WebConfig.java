package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Überschreibt die CORS-Konfiguration, um Anfragen aus bestimmten Quellen zuzulassen
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Erlaubt CORS für alle Endpunkte
                .allowedOriginPatterns("http://localhost:[*]", "https://localhost:[*]") // Erlaubt Anfragen von localhost mit beliebigen Ports
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Beschränkt die erlaubten HTTP-Methoden
                .allowedHeaders("*") // Erlaubt alle Header in den Anfragen
                .allowCredentials(true); // Erlaubt die Übertragung von Credentials (wie Cookies, HTTP-Authentication-Informationen)
    }
}
