package com.example.rental.infrastructure.adapter;

import com.example.rental.application.mapper.PropertyMapper;
import com.example.rental.domain.model.Property;
import com.example.rental.domain.port.PropertyRepositoryPort;
import com.example.rental.infrastructure.entity.PropertyEntity;
import com.example.rental.infrastructure.repository.JpaPropertyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PropertyRepositoryImpl implements PropertyRepositoryPort {

    private final JpaPropertyRepository jpaPropertyRepository;

    public PropertyRepositoryImpl(JpaPropertyRepository jpaPropertyRepository) {
        this.jpaPropertyRepository = jpaPropertyRepository;
    }

    @Override
    public Property save(Property property) {
        PropertyEntity entity = PropertyMapper.toEntity(property);
        return PropertyMapper.toModel(jpaPropertyRepository.save(entity));
    }

    @Override
    public Optional<Property> findById(Long id) {
        return jpaPropertyRepository.findById(id)
                .map(PropertyMapper::toModel);
    }

    @Override
    public List<Property> findAll() {
        return jpaPropertyRepository.findAll().stream()
                .map(PropertyMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Property property) {
        PropertyEntity entity = PropertyMapper.toEntity(property);
        entity.setDeleted(true);
        jpaPropertyRepository.save(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaPropertyRepository.existsByName(name);
    }
    @Override
    public List<Property> findByPriceBetween(double minPrice, double maxPrice) {
        return jpaPropertyRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(PropertyMapper::toModel)
                .collect(Collectors.toList());
    }
}
