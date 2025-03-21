package com.hseok.vote.security.jwt;

import com.hseok.vote.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class JwtGenerator {
    private final SecretKey AccessKey;
    private final SecretKey RefreshKey;
    private final long accessTokenExpTime;
    private final long RefreshTokenExpTime;

    public JwtGenerator(
            @Value("${jwt.secretAccessKey}") String secretAccessKey,
            @Value("${jwt.secretRefreshKey}") String secretRefreshKey,
            @Value("${jwt.accessExpTime}") long accessTokenExpTime,
            @Value("${jwt.refreshExpTime}") long RefreshTokenExpTime
    ) {
        this.AccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
        this.RefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
        this.accessTokenExpTime = accessTokenExpTime;
        this.RefreshTokenExpTime = RefreshTokenExpTime;
    }
    //AccessToken 생성
    public String generateAccessToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(user.getUsername())
                .claims(createClaims(user))
                .expiration(new Date(now + accessTokenExpTime))
                .signWith(AccessKey)
                .compact();
    }

    //RefreshToken 생성
    public String generateRefreshToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(user.getUsername())
                .claims(createClaims(user))
                .expiration(new Date(now + RefreshTokenExpTime))
                .signWith(RefreshKey)
                .compact();
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Identifier", user.getId());
        claims.put("Role", user.getRoles());
        return claims;
    }

}
