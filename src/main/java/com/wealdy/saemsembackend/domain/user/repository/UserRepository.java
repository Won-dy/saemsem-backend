package com.wealdy.saemsembackend.domain.user.repository;

import com.wealdy.saemsembackend.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public List<User> findByLoginId(String id) {
        return em.createQuery("select u from User u where u.loginId = :id", User.class)
            .setParameter("id", id)
            .getResultList();
    }

    public List<User> findByNickname(String nickname) {
        return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
            .setParameter("nickname", nickname)
            .getResultList();
    }
}
