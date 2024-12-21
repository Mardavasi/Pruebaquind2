package com.example.rental.infrastructure.repository;

import com.example.rental.infrastructure.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPropertyRepository extends JpaRepository<PropertyEntity, Long> {
    boolean existsByName(String name);
    List<PropertyEntity> findByPriceBetween(double minPrice, double maxPrice);
}