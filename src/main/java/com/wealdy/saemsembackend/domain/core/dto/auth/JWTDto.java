package com.wealdy.saemsembackend.domain.core.dto.auth;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTDto {

    private long userId;
    private Date expirationTime;
    private Date createdTime;
    private String token;
}
