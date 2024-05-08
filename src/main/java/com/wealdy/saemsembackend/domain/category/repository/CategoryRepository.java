package com.wealdy.saemsembackend.domain.category.repository;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    void save(Category category);

    List<Category> findAll();

    Optional<Category> findByName(String name);
}
