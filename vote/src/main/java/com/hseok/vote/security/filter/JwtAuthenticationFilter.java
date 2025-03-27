package com.hseok.vote.security.filter;
import com.hseok.vote.security.jwt.JwtRule;
import com.hseok.vote.security.jwt.JwtService;
import com.hseok.vote.user.UserPrincipal;
import com.hseok.vote.user.UserService;
import com.hseok.vote.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/login")) {
            log.info("login path이므로 jwt 필터 pass");
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("[JWT Filter] 서비스 시작");

        String accessToken = jwtService.resolveTokenFromCookie(request, JwtRule.ACCESS_PREFIX);
        if (accessToken != null) {
            log.info("[JWT Filter] 액세스 토큰 검증 시작..");

            if(jwtService.validateAccessToken(accessToken)) {
                log.info("[JWT Filter] 액세스 토큰 검증 성공!");
                setAuthentication(accessToken);
                filterChain.doFilter(request, response);
                return;
            }
            else {
                log.info("[JWT Filter] 액세스 토큰이 유효하지 않거나 만료됨");
            }
        } else {
            log.info("[JWT Filter] 쿠키에서 액세스 토큰을 발견할 수 없음");
        }

        String refreshToken = jwtService.resolveTokenFromCookie(request, JwtRule.REFRESH_PREFIX);

        if (refreshToken != null) {
            log.info("[JWT Filter] 리프레쉬 토큰 검증 시작..");
            User user = jwtService.getUserFromRefreshToken(refreshToken);

            if(jwtService.validateRefreshToken(refreshToken, user)) {
                log.info("[JWT Filter] 리프레쉬 토큰 검증 성공!");
                String NewAccessToken = jwtService.createAccessToken(response, user);
                jwtService.createRefreshToken(response, user);
                setAuthentication(NewAccessToken);
                filterChain.doFilter(request, response);
            } else {
                log.info("[JWT Filter] 리프레쉬 토큰이 유효하지 않거나 만료됨");
            }
        } else {
            log.info("[JWT Filter] 쿠키에서 리프레쉬 토큰을 발견할 수 없음");
        }
        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
