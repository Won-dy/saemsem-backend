package com.wealdy.saemsembackend.domain.core.dto.auth;

import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTDto {

    private final long userId;
    private final Date expirationTime;
    private final Date createdTime;
    private final String token;

    public static JWTDto of(long userId, Date expirationTime, Date createdTime, String token) {
        return new JWTDto(userId, expirationTime, createdTime, token);
    }
}
