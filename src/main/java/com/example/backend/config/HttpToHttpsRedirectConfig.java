package com.example.backend.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Markiert die Klasse als Konfigurationsklasse für Spring
@Configuration
public class HttpToHttpsRedirectConfig {

    // Definiert eine Bean für die Servlet-Web-Server-Factory
    @Bean
    public ServletWebServerFactory servletContainer() {
        // Erstellt eine Instanz der Tomcat-Servlet-Web-Server-Factory
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        // Fügt einen zusätzlichen Connector hinzu, der HTTP-Anfragen entgegennimmt und umleitet
        tomcat.addAdditionalTomcatConnectors(httpConnector());

        return tomcat;
    }

    // Definiert eine Bean für den HTTP-Connector
    @Bean
    public Connector httpConnector() {
        // Erstellt einen neuen Connector, der das Standardprotokoll von Tomcat verwendet (HTTP/1.1)
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);

        // Setzt den Connector so, dass er über HTTP an Port 8080 Anfragen entgegennimmt
        connector.setScheme("http");
        connector.setPort(8080);

        // Markiert den Connector als nicht sicher
        connector.setSecure(false);

        // Legt den Port für die Umleitung auf HTTPS fest (Port 8443)
        connector.setRedirectPort(8443);

        return connector;
    }
}
