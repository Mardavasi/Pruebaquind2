package com.example.rental.domain.service;

import com.example.rental.domain.exception.ValidationException;
import com.example.rental.domain.model.Property;
import com.example.rental.domain.port.PropertyRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepositoryPort propertyRepository;
    private final List<String> validLocations = Arrays.asList("Medellin", "Bogota", "Cali", "Cartagena");

    public PropertyService(PropertyRepositoryPort propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property registerProperty(Property property) {
        System.out.println("Registrando propiedad: " + property.getName());
        validateProperty(property);
        Property savedProperty = propertyRepository.save(property);
        System.out.println("Propiedad registrada con ID: " + savedProperty.getId());
        return savedProperty;
    }

    public Property editProperty(Long id, Property updatedProperty) {
        System.out.println("Editando propiedad con ID: " + id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException("La propiedad no existe"));

        if (!existingProperty.isAvailable()) {
            throw new ValidationException("No se puede editar una propiedad que ya fue arrendada");
        }

        validateEditProperty(updatedProperty);
        validateLocationAndPrice(existingProperty.getLocation(), updatedProperty.getPrice());

        existingProperty.setName(updatedProperty.getName());
        existingProperty.setImageUrl(updatedProperty.getImageUrl());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setAvailable(updatedProperty.isAvailable());

        Property updated = propertyRepository.save(existingProperty);
        System.out.println("Propiedad editada con ID: " + updated.getId());
        return updated;
    }

    public void deleteProperty(Long id) {
        System.out.println("Intentando eliminar propiedad con ID: " + id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException("La propiedad no existe"));

        if (existingProperty.isDeleted()) {
            System.out.println("La propiedad con ID " + id + " ya está eliminada");
            throw new ValidationException("La propiedad ya fue eliminada anteriormente");
        }

        if (existingProperty.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(1))) {
            throw new ValidationException("No se puede eliminar propiedades con más de un mes de antigüedad");
        }

        existingProperty.setDeleted(true);
        propertyRepository.save(existingProperty);
        System.out.println("Propiedad con ID " + id + " eliminada lógicamente");
    }

    public Property rentProperty(Long id) {
        System.out.println("Arrendando propiedad con ID: " + id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException("La propiedad no existe"));

        if (!existingProperty.isAvailable()) {
            throw new ValidationException("La propiedad no está disponible para arrendar");
        }

        existingProperty.setAvailable(false);
        Property rented = propertyRepository.save(existingProperty);
        System.out.println("Propiedad con ID " + id + " arrendada correctamente");
        return rented;
    }

    private void validateProperty(Property property) {
        if (property.getName() == null || property.getName().isEmpty()) {
            throw new ValidationException("El nombre de la propiedad no puede estar vacío");
        }
        validateLocationAndPrice(property.getLocation(), property.getPrice());
    }

    private void validateEditProperty(Property updatedProperty) {
        if (updatedProperty.getName() == null || updatedProperty.getName().isEmpty()) {
            throw new ValidationException("El nombre de la propiedad no puede estar vacío al editar");
        }
        if (updatedProperty.getPrice() <= 0) {
            throw new ValidationException("El precio debe ser mayor a 0 al editar");
        }
        if (updatedProperty.getImageUrl() == null || updatedProperty.getImageUrl().isEmpty()) {
            throw new ValidationException("La URL de la imagen no puede estar vacía al editar");
        }
    }

    private void validateLocationAndPrice(String location, double price) {
        if (!validLocations.contains(location)) {
            throw new ValidationException("La ubicación no es válida. Ubicaciones válidas: " + validLocations);
        }
        if ((location.equals("Bogota") || location.equals("Cali")) && price < 2000000) {
            throw new ValidationException("El precio debe ser mayor a 2,000,000 en Bogotá o Cali");
        }
    }
}

