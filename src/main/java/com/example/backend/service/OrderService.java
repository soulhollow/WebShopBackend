package com.example.backend.service;

import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.OrderItemDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.User;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    // Alle Bestellungen abrufen
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Bestellung nach ID abrufen
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Bestellung erstellen
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus("PENDING");
        order.setTotalAmount(calculateTotalAmount(orderDTO.getOrderItems()));

        Order savedOrder = orderRepository.save(order);

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden")));
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(orderItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }

    // Bestellungen eines Benutzers basierend auf der User-ID abrufen
    @Transactional
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return orderRepository.findByUser(user);
        }
        return List.of(); // Leere Liste zurückgeben, wenn der Benutzer nicht existiert
    }

    // Bestellstatus aktualisieren
    @Transactional
    public Optional<Order> updateOrderStatus(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            order.setOrderStatus(status);
            return orderRepository.save(order);
        });
    }

    // Bestellungen eines Benutzers basierend auf dem Benutzernamen abrufen
    @Transactional
    public List<Order> getOrdersByUsername(String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return orderRepository.findByUser(user);
        }
        return List.of(); // Leere Liste zurückgeben, wenn der Benutzer nicht existiert
    }

    // Bestellung löschen
    @Transactional
    public boolean deleteOrder(Long id) {
        return orderRepository.findById(id).map(order -> {
            orderRepository.delete(order);
            return true;
        }).orElse(false);
    }

    // Gesamtbetrag der Bestellung berechnen
    private BigDecimal calculateTotalAmount(List<OrderItemDTO> orderItems) {
        return orderItems.stream()
                .map(item -> productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden"))
                        .getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
