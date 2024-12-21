package com.example.rental.application.usecase;

import com.example.rental.domain.exception.ValidationException;
import com.example.rental.domain.model.Property;
import com.example.rental.domain.port.PropertyRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUseCase.class);
    private static final String PROPERTY_NOT_FOUND = "La propiedad no existe";
    private static final String PROPERTY_ALREADY_DELETED = "La propiedad ya fue eliminada anteriormente";
    private static final String PROPERTY_TOO_OLD = "No se puede eliminar propiedades con más de un mes de antigüedad";
    static final String PROPERTY_NOT_AVAILABLE = "No se puede editar una propiedad que ya fue arrendada";
    private static final String PROPERTY_ALREADY_EXISTS = "Ya existe una propiedad con el nombre '%s'";
    private static final String PROPERTY_NAME_EMPTY = "El nombre de la propiedad no puede estar vacío";
    private static final String PROPERTY_PRICE_INVALID = "El precio debe ser mayor a 0 al editar";
    static final String PROPERTY_IMAGE_URL_EMPTY = "La URL de la imagen no puede estar vacía";
    static final String LOCATION_INVALID = "La ubicación no es válida. Ubicaciones válidas: %s";
    static final String PRICE_TOO_LOW = "El precio debe ser mayor a 2,000,000 en Bogotá o Cali";

    private final PropertyRepositoryPort propertyRepository;
    private final List<String> validLocations = Arrays.asList("Medellin", "Bogota", "Cali", "Cartagena");

    public PropertyUseCase(PropertyRepositoryPort propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> listProperties(Optional<Double> minPrice, Optional<Double> maxPrice) {
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            return propertyRepository.findByPriceBetween(minPrice.get(), maxPrice.get());
        } else {
            return propertyRepository.findAll();
        }
    }

    public Property registerProperty(Property property) {
        logger.info("Registrando propiedad: {}", property.getName());
        validateProperty(property);

        if (propertyRepository.existsByName(property.getName())) {
            throw new ValidationException(String.format(PROPERTY_ALREADY_EXISTS, property.getName()));
        }

        property.setCreatedAt(LocalDateTime.now());
        Property savedProperty = propertyRepository.save(property);
        logger.info("Propiedad registrada con ID: {}", savedProperty.getId());
        return savedProperty;
    }

    public Property editProperty(Long id, Property updatedProperty) {
        logger.info("Editando propiedad con ID: {}", id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException(PROPERTY_NOT_FOUND));

        if (!existingProperty.isAvailable()) {
            throw new ValidationException(PROPERTY_NOT_AVAILABLE);
        }

        validateEditProperty(existingProperty, updatedProperty);

        existingProperty.setName(updatedProperty.getName());
        existingProperty.setImageUrl(updatedProperty.getImageUrl());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setAvailable(updatedProperty.isAvailable());

        Property updated = propertyRepository.save(existingProperty);
        logger.info("Propiedad editada con ID: {}", updated.getId());
        return updated;
    }

    public void deleteProperty(Long id) {
        logger.info("Intentando eliminar propiedad con ID: {}", id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException(PROPERTY_NOT_FOUND));

        if (existingProperty.isDeleted()) {
            throw new ValidationException(PROPERTY_ALREADY_DELETED);
        }

        if (existingProperty.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(1))) {
            throw new ValidationException(PROPERTY_TOO_OLD);
        }

        existingProperty.setDeleted(true);
        propertyRepository.save(existingProperty);
        logger.info("Propiedad con ID {} eliminada lógicamente", id);
    }

    public Property rentProperty(Long id) {
        logger.info("Arrendando propiedad con ID: {}", id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ValidationException(PROPERTY_NOT_FOUND));

        if (!existingProperty.isAvailable()) {
            throw new ValidationException("La propiedad no está disponible para arrendar");
        }

        existingProperty.setAvailable(false);
        Property rented = propertyRepository.save(existingProperty);
        logger.info("Propiedad con ID {} arrendada correctamente", id);
        return rented;
    }

    private void validateProperty(Property property) {
        if (property.getName() == null || property.getName().isEmpty()) {
            throw new ValidationException(PROPERTY_NAME_EMPTY);
        }
        validateLocationAndPrice(property.getLocation(), property.getPrice());
        if (property.getImageUrl() == null || property.getImageUrl().isEmpty()) {
            throw new ValidationException(PROPERTY_IMAGE_URL_EMPTY);
        }
    }

    private void validateEditProperty(Property existingProperty, Property updatedProperty) {
        if (updatedProperty.getName() == null || updatedProperty.getName().isEmpty()) {
            throw new ValidationException(PROPERTY_NAME_EMPTY);
        }
        if (updatedProperty.getPrice() <= 0) {
            throw new ValidationException(PROPERTY_PRICE_INVALID);
        }
        if (!existingProperty.isAvailable()) {
            throw new ValidationException(PROPERTY_NOT_AVAILABLE);
        }
        if ((updatedProperty.getPrice() < 2000000) &&
                (updatedProperty.getLocation().equals("Bogota") || updatedProperty.getLocation().equals("Cali"))) {
            throw new ValidationException(PRICE_TOO_LOW);
        }
        if (updatedProperty.getImageUrl() == null || updatedProperty.getImageUrl().isEmpty()) {
            throw new ValidationException(PROPERTY_IMAGE_URL_EMPTY);
        }
    }

    void validateLocationAndPrice(String location, double price) {
        if (!validLocations.contains(location)) {
            throw new ValidationException(String.format(LOCATION_INVALID, validLocations));
        }
        if ((location.equals("Bogota") || location.equals("Cali")) && price < 2000000) {
            throw new ValidationException(PRICE_TOO_LOW);
        }
    }
}