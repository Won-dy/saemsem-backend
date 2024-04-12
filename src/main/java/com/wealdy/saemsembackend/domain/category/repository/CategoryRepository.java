package com.wealdy.saemsembackend.domain.category.repository;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_CATEGORY;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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

    public Category findByName(String name) {
        // TODO: NoResultException 이 orElseThrow 까지 안오고 getSingleResult() 에서 에러 발생
//        return Optional.ofNullable(em.createQuery("select c from Category c where c.name=:name", Category.class)
//            .setParameter("name", name)
//            .getSingleResult()).orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY));

        TypedQuery<Category> query = em.createQuery("select c from Category c where c.name=:name", Category.class)
            .setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(NOT_FOUND_CATEGORY);
        }
    }
}
