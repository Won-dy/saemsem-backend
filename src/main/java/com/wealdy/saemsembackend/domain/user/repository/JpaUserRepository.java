package com.wealdy.saemsembackend.domain.user.repository;

import com.wealdy.saemsembackend.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findByLoginId(String id) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.loginId = :id", User.class)
            .setParameter("id", id);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByNickname(String nickname) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.nickname = :nickname", User.class)
            .setParameter("nickname", nickname);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByLoginIdAndPassword(String id, String password) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.loginId = :id and u.password = :password", User.class)
            .setParameter("id", id)
            .setParameter("password", password);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
