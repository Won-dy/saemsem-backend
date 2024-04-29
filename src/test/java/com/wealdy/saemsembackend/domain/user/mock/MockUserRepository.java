package com.wealdy.saemsembackend.domain.user.mock;

import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.removeIf(removeUser -> removeUser.getLoginId().equals(user.getLoginId()));
        users.add(user);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return users.stream()
            .filter(user -> user.getLoginId().equals(loginId))
            .findFirst();
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return users.stream()
            .filter(user -> user.getNickname().equals(nickname))
            .findFirst();
    }

    @Override
    public Optional<User> findByLoginIdAndPassword(String loginId, String password) {
        return users.stream()
            .filter(user -> user.getLoginId().equals(loginId))
            .filter(user -> user.getPassword().equals(password))
            .findFirst();
    }
}
