package com.wealdy.saemsembackend.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.core.exception.AlreadyExistException;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.mock.MockUserRepository;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[UserServiceTest]")
class UserServiceTest {

    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void init() {
        userRepository = new MockUserRepository();
        userService = new UserService(userRepository);
    }

    @DisplayName("[join] 회원가입을 성공한다.")
    @Test
    void join() {
        // given
        userService.join("loginId", "1234", "nickname");

        // when
        Optional<User> joinUser = userRepository.findByLoginId("loginId");

        // then
        assertThat(joinUser.isPresent()).isTrue();
        assertThat(joinUser.get().getLoginId()).isEqualTo("loginId");
        assertThat(joinUser.get().getPassword()).isEqualTo("1234");
        assertThat(joinUser.get().getNickname()).isEqualTo("nickname");
    }

    @DisplayName("[validateDuplicateId] 이미 존재하는 아이디로 회원가입한다.")
    @Test
    void validateDuplicateId() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(user);

        // when
        AlreadyExistException alreadyExistException = Assertions.catchThrowableOfType(
            () -> userService.join("loginId", "1234", "nickname2"),
            AlreadyExistException.class
        );

        // then
        assertThat(alreadyExistException.getCode()).isEqualTo(ResponseCode.ALREADY_EXIST_ID.getCode());
    }

    @DisplayName("[validateDuplicateNickname] 이미 존재하는 닉네임으로 회원가입한다.")
    @Test
    void validateDuplicateNickname() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(user);

        // when
        AlreadyExistException alreadyExistException = Assertions.catchThrowableOfType(
            () -> userService.join("loginId2", "1234", "nickname"),
            AlreadyExistException.class
        );

        // then
        assertThat(alreadyExistException.getCode()).isEqualTo(ResponseCode.ALREADY_EXIST_NICKNAME.getCode());
    }

    @DisplayName("[getUser] loginId 로 User 조회를 성공한다.")
    @Test
    void getUser() {
        // given
        User newUser = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(newUser);

        // when
        User user = userService.getUser("loginId");

        // then
        assertThat(user.getLoginId()).isEqualTo("loginId");
    }

    @DisplayName("[getUserFail] loginId 로 User 조회를 실패한다.")
    @Test
    void getUserFail() {
        // given
        User newUser = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(newUser);

        // when
        NotFoundException notFoundException = Assertions.catchThrowableOfType(
            () -> userService.getUser("loginId2"),
            NotFoundException.class
        );

        // then
        assertThat(notFoundException.getCode()).isEqualTo(ResponseCode.NOT_FOUND_USER.getCode());
    }
}