package com.wealdy.saemsembackend.domain.core.util;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

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
import javax.crypto.SecretKey;

public class JwtUtil {

    private final static String secret = "djtyS5dopy5dfNt9dfgPmwch5d6klsg0sdlk1kYDgp";
    private final static String PREFIX_BEARER = "Bearer ";
    private final static int ACCESS_TOKEN_EXPIRATION = 86400;  // 1 DAY

    public String createAccessToken(long userId) {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION);
    }

    private String createToken(long userId, long expiration) {
        Date now = new Date();
        return Jwts.builder()
            .claim(USER_ID_KEY, userId)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + (1000 * expiration)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();
    }

    public JWTDto parseToken(String tokenString) {
        String token = removePrefix(tokenString);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);
            Claims claims = claimsJws.getPayload();

            return JWTDto.of(claims.get(USER_ID_KEY, Long.class), claims.getExpiration(), claims.getIssuedAt(), token);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException();
        } catch (JwtException ex) {
            throw new InvalidTokenException();
        }
    }

    private String removePrefix(String token) {
        if (token.startsWith(PREFIX_BEARER)) {
            return token.substring(7);
        }
        return token;
    }
}
