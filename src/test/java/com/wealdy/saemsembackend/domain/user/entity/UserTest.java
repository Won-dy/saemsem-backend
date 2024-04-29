package com.wealdy.saemsembackend.domain.user.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[UserTest]")
class UserTest {

    @DisplayName("[createUser] 유저를 생성한다.")
    @Test
    void createUser() {
        // given
        // when
        User user = User.createUser("loginId", "1234", "nickname");

        // then
        assertThat(user.getLoginId()).isEqualTo("loginId");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getNickname()).isEqualTo("nickname");
    }
}