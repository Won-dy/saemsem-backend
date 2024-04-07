package com.wealdy.saemsembackend.domain.user.service;

import com.wealdy.saemsembackend.domain.core.exception.AlreadyExistException;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.JwtUtil;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import com.wealdy.saemsembackend.domain.user.service.dto.GetLoginDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void join(String loginId, String password, String nickname) {
        validateDuplicateId(loginId);
        validateDuplicateNickname(nickname);

        User newUser = User.createUser(loginId, password, nickname);
        userRepository.save(newUser);
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

    public GetLoginDto login(String id, String password) {
        List<User> loginUser = userRepository.findByLoginIdAndPassword(id, password);
        if (loginUser.isEmpty()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_USER);
        }

        JwtUtil jwtUtil = new JwtUtil();
        Long userId = loginUser.get(0).getId();
        String accessToken = jwtUtil.createAccessToken(userId);
        return new GetLoginDto(accessToken);
    }
}
