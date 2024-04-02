package com.wealdy.saemsembackend.domain.core.util;

import static com.wealdy.saemsembackend.domain.core.Constant.ACCESS_TOKEN_EXPIRATION;
import static com.wealdy.saemsembackend.domain.core.Constant.PREFIX_BEARER;
import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.dto.auth.JWTDto;
import com.wealdy.saemsembackend.domain.core.exception.ExpiredTokenException;
import com.wealdy.saemsembackend.domain.core.exception.InvalidTokenException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
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

            return new JWTDto(claims.get(USER_ID_KEY, Long.class), claims.getExpiration(), claims.getIssuedAt(), token);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException(ResponseCode.EXPIRED_TOKEN);
        } catch (JwtException ex) {
            throw new InvalidTokenException(ResponseCode.INVALID_TOKEN);
        }
    }

    private String removePrefix(String token) {
        if (token.startsWith(PREFIX_BEARER)) {
            return token.substring(7);
        }
        return token;
    }
}
