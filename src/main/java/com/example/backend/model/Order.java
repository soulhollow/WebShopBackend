package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "orders")  // Definiert die Tabelle, die dieser Entität zugeordnet ist
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primärschlüssel mit automatischer Generierung
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Viele-zu-Eins-Beziehung mit der User-Entität
    @JoinColumn(name = "user_id", nullable = false)  // Fremdschlüsselspalte, die den Benutzer identifiziert
    @JsonIgnoreProperties({"orders", "hibernateLazyInitializer", "handler"})  // Ignoriert bestimmte Eigenschaften bei der Serialisierung
    private User user;

    @Column(nullable = false, length = 20)  // Spalte für den Status der Bestellung, max. Länge 20 Zeichen
    private String orderStatus;

    @Column(nullable = false)  // Spalte für den Gesamtbetrag der Bestellung, darf nicht null sein
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)  // Erstellt eine unveränderbare Spalte für das Erstellungsdatum
    @Temporal(TemporalType.TIMESTAMP)  // Speichert das Datum und die Zeit des Erstellens
    private Date createdAt = new Date();

    @Column(name = "updated_at")  // Spalte für das letzte Aktualisierungsdatum
    @Temporal(TemporalType.TIMESTAMP)  // Speichert das Datum und die Zeit der letzten Aktualisierung
    private Date updatedAt = new Date();

    // Setzt das `updatedAt`-Feld vor jeder Aktualisierung automatisch auf das aktuelle Datum
    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = new Date();
    }

    // Getter und Setter für die Felder

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
