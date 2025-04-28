package com.hseok.vote.security.jwt;

import com.hseok.vote.user.CustomUserDetailsService;
import com.hseok.vote.user.UserPrincipal;
import com.hseok.vote.user.UserService;
import com.hseok.vote.user.domain.RefreshToken;
import com.hseok.vote.user.domain.User;
import com.hseok.vote.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;

import static com.hseok.vote.security.jwt.JwtRule.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtGenerator jwtGenerator;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public String createAccessToken(HttpServletResponse response, User user) {
        String accessToken = jwtGenerator.generateAccessToken(user);
        ResponseCookie cookie = jwtGenerator.createTokenCookie(ACCESS_PREFIX.getValue(), accessToken);
        response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

        log.info("Access Token 발급 성공");
        return accessToken;
    }

    public String createAccessToken(HttpServletResponse response, UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getUserId());
        return createAccessToken(response, user);
    }

    @Transactional
    public String createRefreshToken(HttpServletResponse response, User user) {
        String refreshToken = jwtGenerator.generateRefreshToken(user);
        ResponseCookie cookie = jwtGenerator.createTokenCookie(REFRESH_PREFIX.getValue(), refreshToken);
        response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.save(new RefreshToken(user, refreshToken, jwtGenerator.getRefreshTokenExpTime()));
        log.info("Refresh Token 발급 및 저장 성공");
        return refreshToken;
    }

    @Transactional
    public String createRefreshToken(HttpServletResponse response, UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getUserId());
        return createRefreshToken(response, user);
    }

    public boolean validateAccessToken(String token) {
        return getTokenStatus(token, ACCESS_PREFIX) == TokenStatus.AUTHENTICATED;
    }

    public boolean validateRefreshToken(String token) {
        return getTokenStatus(token, REFRESH_PREFIX) == TokenStatus.AUTHENTICATED;
    }

    public boolean validateRefreshTokenWithUser(String token, User user) {
        RefreshToken storedToken = refreshTokenRepository.findTopByUserOrderByIdDesc(user).orElseThrow();
        return storedToken.getToken().equals(token);

    }


    private TokenStatus getTokenStatus(String token, JwtRule jwtRule) {
        try {
            SecretKey key = jwtRule == ACCESS_PREFIX ? jwtGenerator.getAccessKey() : jwtGenerator.getRefreshKey();

            Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token);
            log.debug("토큰검증:성공");
            return TokenStatus.AUTHENTICATED;
        } catch (ExpiredJwtException e) {
            log.debug("토큰검증:만료");
            return TokenStatus.EXPIRED;
        } catch (JwtException e) {
            log.debug("토큰검증:에러");
            return TokenStatus.INVALID;
        }
    }

    public String getUsernameFromAccessToken(String token) {
        try {
            return Jwts.parser().verifyWith(jwtGenerator.getAccessKey()).build()
                    .parseSignedClaims(token).getPayload().getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }

    }

    public String getUsernameFromRefreshToken(String token) {
        try {
            return Jwts.parser().verifyWith(jwtGenerator.getRefreshKey()).build()
                    .parseSignedClaims(token).getPayload().getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }

    }

    public User getUserFromRefreshToken(String token) {
        return userService.getUserByUsername(getUsernameFromRefreshToken(token));
    }

    /// TODO: 매 요청시마다 db요청 부하 발생. jwt토큰에 포함된 정보만으로 principal을 생성하는 방식 검토
    public Authentication getAuthentication(String token) {
        UserDetails principal = customUserDetailsService.loadUserByUsername(getUsernameFromAccessToken(token));
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    public String resolveTokenFromCookie(HttpServletRequest request, JwtRule tokenPrefix) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @Transactional
    public void logout(User user) {
        refreshTokenRepository.deleteByUser(user);
        log.info("[JWT Service] RefreshToken 무효화 처리됨: " + user.getUsername());
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String refreshToken = resolveTokenFromCookie(request, JwtRule.REFRESH_PREFIX);
        User user = getUserFromRefreshToken(refreshToken);
        log.info(user.getUsername());

        logout(user);
    }


}
