package com.example.rental.infrastructure.repository;

import com.example.rental.infrastructure.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPropertyRepository extends JpaRepository<PropertyEntity, Long> {
    // Métodos adicionales si es necesario
}