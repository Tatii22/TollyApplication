package com.rentaherramientas.tolly.domain.ports;
import com.rentaherramientas.tolly.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    boolean existsByName(String name);
    void deleteById(Long id);
    Optional<Category> update(Long id, Category category);
}
