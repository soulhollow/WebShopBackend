package com.example.backend.service;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Holt alle Produkte aus der Datenbank
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Sucht nach einem Produkt basierend auf der ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Erstellt ein neues Produkt in der Datenbank
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Aktualisiert ein bestehendes Produkt basierend auf der ID und den übergebenen Details
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            return productRepository.save(product);
        });
    }

    // Löscht ein Produkt basierend auf der ID, falls es existiert
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }
}
