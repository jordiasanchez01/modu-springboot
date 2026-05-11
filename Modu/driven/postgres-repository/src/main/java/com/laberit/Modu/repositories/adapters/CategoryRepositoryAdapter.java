package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import com.laberit.Modu.repositories.CategoryJpaRepository;
import com.laberit.Modu.repositories.mappers.CategoryPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {
    private final CategoryJpaRepository repository;
    private final CategoryPersistanceMapper mapper;

    @Override
    public List<Category> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public Set<Category> findAllByIdIn(Set<Integer> id) {
        return new HashSet<>((mapper.toDomainList(repository.findAllById(id))));
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Category save(Category category) {
        return mapper.toDomain(repository.save(mapper.toEntity(category)));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
