package com.example.rental.infrastructure.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private boolean available;
    private String imageUrl;
    private double price;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
    }

    // Getters y Setters
}