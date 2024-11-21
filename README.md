#WebShop Backen

Voraussetzungen
Stellen Sie sicher, dass folgende Software installiert ist:

Java Development Kit (JDK) 17 oder höher
Maven
MySQL-Datenbank
Optional: Postman oder ein ähnliches Tool zum Testen von APIs
Installation
Wechseln Sie in die Anwendung und führen Sie folgenden Befhl aus: mvn clean install

Die wichtigsten Konfigurationseinstellungen befinden sich in der Datei application.properties. Hier sind einige wesentliche Parameter:

Datenbankverbindung: Die Anwendung verwendet eine MySQL-Datenbank. Stellen Sie sicher, dass die Datenbank läuft und die Verbindungsinformationen korrekt sind.

spring.datasource.url=jdbc:mysql://localhost:3306/database_aiagent
spring.datasource.username=root
spring.datasource.password=
JPA-Einstellungen: Die Hibernate-Einstellungen definieren, wie das Datenbank-Schema behandelt wird.

HTTPS-Konfiguration: Die Anwendung läuft über HTTPS auf Port 8443. Da dies kein offizielles Zertifikat ist, müssen sie auf https://localhost:8443/api und dieser Vertrauen.

server.port=8443 server.ssl.key-store=classpath:keystore.p12 server.ssl.key-store-password=admin1

MySQL-Datenbank erstellen: Stellen Sie sicher, dass eine MySQL-Datenbank budget_management vorhanden ist. Führen sie dafür den Befehl: CREATE DATABASE database_aiagent;

Datenbank-Benutzer konfigurieren: Passen Sie den Benutzernamen und das Passwort in der application.properties-Datei entsprechend an.

Anmeldedaten, wenn testdaten genutzt werden: admin admin
