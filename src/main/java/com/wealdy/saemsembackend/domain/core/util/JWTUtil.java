package com.wealdy.saemsembackend.domain.core.util;

import static com.wealdy.saemsembackend.domain.core.Constant.ACCESS_TOKEN_EXPIRATION;
import static com.wealdy.saemsembackend.domain.core.Constant.JWT_CLAIM_NAME_USER_ID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String createAccessToken(long userId) {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION);
    }

    private String createToken(long userId, long expiration) {
        Date now = new Date();
        return Jwts.builder()
            .claim(JWT_CLAIM_NAME_USER_ID, userId)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + (1000 * expiration)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();
    }
}
