package com.wealdy.saemsembackend.domain.core.util;

import static com.wealdy.saemsembackend.domain.core.Constant.LOGIN_ID_KEY;

import com.wealdy.saemsembackend.domain.core.dto.auth.JWTDto;
import com.wealdy.saemsembackend.domain.core.exception.ExpiredTokenException;
import com.wealdy.saemsembackend.domain.core.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    private static JwtUtil instance;

    public static JwtUtil getInstance() {
        if (Objects.isNull(instance)) {
            instance = new JwtUtil();
        }
        return instance;
    }

    private final static String secret = "djtyS5dopy5dfNt9dfgPmwch5d6klsg0sdlk1kYDgp";
    private final static int ACCESS_TOKEN_EXPIRATION = 86400;  // 1 DAY

    public String createAccessToken(String loginId) {
        return createToken(loginId, ACCESS_TOKEN_EXPIRATION);
    }

    private String createToken(String loginId, long expiration) {
        Date now = new Date();
        return Jwts.builder()
            .claim(LOGIN_ID_KEY, loginId)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + (1000 * expiration)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();
    }

    public JWTDto parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);
            Claims claims = claimsJws.getPayload();

            return JWTDto.of(claims.get(LOGIN_ID_KEY, String.class), claims.getExpiration(), claims.getIssuedAt(), token);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException();
        } catch (JwtException ex) {
            throw new InvalidTokenException();
        }
    }
}
