package com.wealdy.saemsembackend.domain.category.repository;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Long countAll() {
        return em.createQuery("select count(c) from Category c", Long.class).getSingleResult();
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }
}
