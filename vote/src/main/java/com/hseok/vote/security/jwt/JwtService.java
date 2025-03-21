package com.hseok.vote.security.jwt;

import com.hseok.vote.user.UserPrincipal;
import com.hseok.vote.user.UserService;
import com.hseok.vote.user.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtService {
    private final JwtGenerator jwtGenerator;
    private final UserService userService;

    public JwtService(
            JwtGenerator jwtGenerator,
            UserService userService
    ) {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    };

    public String createAccessToken(HttpServletResponse response, UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getUserId());
        String accessToken = jwtGenerator.generateAccessToken(user);

        return accessToken;
    }

    public String createRefreshToken(HttpServletResponse response, UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getUserId());
        String refreshToken = jwtGenerator.generateRefreshToken(user);

        return refreshToken;
    }

    public boolean validateToken(String token, boolean isAccessToken) {
        return getTokenStatus(token, isAccessToken) == TokenStatus.AUTHENTICATED;
    }

    private TokenStatus getTokenStatus(String token, boolean isAccessToken) {
        try {
            SecretKey key = isAccessToken ? jwtGenerator.getAccessKey() : jwtGenerator.getRefreshKey();

            Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token);
            log.info("토큰검증:성공");
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            log.info("토큰검증:만료");
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            log.info("토큰검증:에러");
            return TokenStatus.INVALID;
        }
    }


}
