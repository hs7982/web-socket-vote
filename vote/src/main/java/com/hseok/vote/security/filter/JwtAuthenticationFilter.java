package com.hseok.vote.security.filter;
import com.hseok.vote.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
            filterChain.doFilter(request, response);
            return;
        }

        String AuthorizationHeader = request.getHeader("Authorization");
        if (AuthorizationHeader != null || AuthorizationHeader.startsWith("Bearer ")) {
            log.info("[JWT Filter] 토큰 검증 시작..");
            String accessToken = AuthorizationHeader.substring(7);

            if(jwtService.validateToken(accessToken, true)) {
                log.info("[JWT Filter] 토큰 검증 성공!");

                //TODO: 유저정보 가져와 context 처리
            }
            else {
                log.info("[JWT Filter] 토큰이 유효하지 않거나 만료됨");
            }
        }
        filterChain.doFilter(request, response);

    }
}
