package com.wealdy.saemsembackend.domain.user.service;

import com.wealdy.saemsembackend.common.exception.AlreadyExistException;
import com.wealdy.saemsembackend.common.response.IdResponseDto;
import com.wealdy.saemsembackend.common.response.ResponseCode;
import com.wealdy.saemsembackend.domain.user.dto.UserDto;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public IdResponseDto join(UserDto.Create user) {
        validateDuplicateId(user.getLoginId());
        validateDuplicateNickname(user.getNickname());

        User newUser = User.createUser(user.getLoginId(), user.getPassword(), user.getNickname());
        userRepository.save(newUser);
        return IdResponseDto.from(newUser.getId());
    }

    private void validateDuplicateId(String loginId) {
        List<User> findUsers = userRepository.findByLoginId(loginId);
        if (!findUsers.isEmpty()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST_ID);
        }
    }

    private void validateDuplicateNickname(String nickname) {
        List<User> findUsers = userRepository.findByNickname(nickname);
        if (!findUsers.isEmpty()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST_NICKNAME);
        }
    }
}
