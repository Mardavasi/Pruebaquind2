package com.example.rental.application.mapper;

import com.example.rental.domain.model.Property;
import com.example.rental.infrastructure.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor

public class PropertyMapper {

    public static PropertyEntity toEntity(Property property) {
        PropertyEntity entity = new PropertyEntity();
        entity.setId(property.getId());
        entity.setName(property.getName());
        entity.setLocation(property.getLocation());
        entity.setAvailable(property.isAvailable());
        entity.setImageUrl(property.getImageUrl());
        entity.setPrice(property.getPrice());
        entity.setDeleted(property.isDeleted());
        return entity;
    }

    public static Property toModel(PropertyEntity entity) {
        Property property = new Property();
        property.setId(entity.getId());
        property.setName(entity.getName());
        property.setLocation(entity.getLocation());
        property.setAvailable(entity.isAvailable());
        property.setImageUrl(entity.getImageUrl());
        property.setPrice(entity.getPrice());
        property.setCreatedAt(entity.getCreatedAt());
        property.setDeleted(entity.isDeleted());
        return property;
    }
}
