package com.example.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")  // Definiert die Datenbanktabelle für Order-Positionen
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primärschlüssel mit automatischer Generierung
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // Viele-zu-Eins-Beziehung zur Order-Entität
    @JoinColumn(name = "order_id", nullable = false)  // Fremdschlüssel zur Bestell-ID, darf nicht null sein
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // Viele-zu-Eins-Beziehung zur Produkt-Entität
    @JoinColumn(name = "product_id", nullable = false)  // Fremdschlüssel zur Produkt-ID, darf nicht null sein
    private Product product;

    @Column(nullable = false)  // Anzahl des Produkts, darf nicht null sein
    private Integer quantity;

    @Column(nullable = false)  // Preis des Produkts, darf nicht null sein
    private BigDecimal price;

    // Getter und Setter für die Felder

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
