package com.example.rental.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    private Long id;
    private String name;
    private String location;
    private boolean available;
    private String imageUrl;
    private double price;
    private LocalDateTime createdAt;
    private boolean deleted;

    // Getters y Setters
}
