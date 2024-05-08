package com.wealdy.saemsembackend.domain.category.repository;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCategoryRepository implements CategoryRepository {

    private final EntityManager em;

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }

    public Optional<Category> findByName(String name) {
        TypedQuery<Category> query = em.createQuery("select c from Category c where c.name=:name", Category.class)
            .setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
