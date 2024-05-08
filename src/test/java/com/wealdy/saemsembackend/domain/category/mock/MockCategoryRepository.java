package com.wealdy.saemsembackend.domain.category.mock;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockCategoryRepository implements CategoryRepository {

    private final List<Category> categoryList = new ArrayList<>();

    public void save(Category category) {
        categoryList.removeIf(removeCategory -> removeCategory.getId().equals(category.getId()));
        categoryList.add(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryList;
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryList.stream()
            .filter(category -> category.getName().equals(name))
            .findFirst();
    }
}
