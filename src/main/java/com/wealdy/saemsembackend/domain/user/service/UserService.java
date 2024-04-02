package com.wealdy.saemsembackend.domain.user.service;

import com.wealdy.saemsembackend.domain.core.exception.AlreadyExistException;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.IdResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.JWTUtil;
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

    private final JWTUtil jwtUtil;

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

    public UserDto.LoginResponse login(UserDto.LoginRequest loginRequest) {
        List<User> loginUser = userRepository.findByLoginIdAndPassword(loginRequest.getLoginId(), loginRequest.getPassword());
        if (loginUser.isEmpty()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_USER);
        }

        Long userId = loginUser.get(0).getId();
        String accessToken = jwtUtil.createAccessToken(userId);
        return new UserDto.LoginResponse(userId, accessToken);
    }
}
