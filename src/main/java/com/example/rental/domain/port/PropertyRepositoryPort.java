package com.example.rental.domain.port;

import com.example.rental.domain.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepositoryPort {
    Property save(Property property);
    Optional<Property> findById(Long id);
    List<Property> findAll();
    List<Property> findByPriceBetween(double minPrice, double maxPrice);
    void delete(Property property);
    boolean existsByName(String name);
}
