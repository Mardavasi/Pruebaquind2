package com.example.rental.infrastructure.controller;

import com.example.rental.domain.model.Property;
import com.example.rental.domain.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> registerProperty(@RequestBody Property property) {
        Property created = propertyService.registerProperty(property);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<?> listProperties(
            @RequestParam Optional<Double> minPrice,
            @RequestParam Optional<Double> maxPrice) {

        try {
            List<Property> properties = propertyService.listProperties(minPrice, maxPrice);
            return ResponseEntity.ok(Map.of(
                    "status", "La solicitud fue exitosa",
                    "properties", properties
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Ocurrió un error al realizar la solicitud",
                    "error", e.getMessage()
            ));
        }
    }

    // Editar Propiedad
    @PutMapping("/{id}")
    public ResponseEntity<Property> editProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {
        Property property = propertyService.editProperty(id, updatedProperty);
        return ResponseEntity.ok(property);
    }

    // Eliminar Propiedad
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Propiedad eliminada lógicamente");
    }

    // Arrendar Propiedad
    @PostMapping("/{id}/rent")
    public ResponseEntity<Property> rentProperty(@PathVariable Long id) {
        Property property = propertyService.rentProperty(id);
        return ResponseEntity.ok(property);
    }


}