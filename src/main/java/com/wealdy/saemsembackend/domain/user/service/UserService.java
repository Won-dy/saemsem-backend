package com.wealdy.saemsembackend.domain.user.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_USER;

import com.wealdy.saemsembackend.domain.core.exception.AlreadyExistException;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.JwtUtil;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import com.wealdy.saemsembackend.domain.user.service.dto.GetLoginDto;
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
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST_ID);
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST_NICKNAME);
        }
    }

    public GetLoginDto login(String id, String password) {
        User user = userRepository.findByLoginIdAndPassword(id, password)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));

        Long userId = user.getId();
        JwtUtil jwtUtil = JwtUtil.getInstance();
        String accessToken = jwtUtil.createAccessToken(userId);
        return new GetLoginDto(accessToken);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }
}
