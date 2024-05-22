package com.wealdy.saemsembackend.domain.user.repository;

import com.wealdy.saemsembackend.domain.user.entity.User;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNickname(String nickname);
}
