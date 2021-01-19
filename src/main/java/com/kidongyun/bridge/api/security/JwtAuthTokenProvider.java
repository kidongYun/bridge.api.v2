package com.kidongyun.bridge.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {
    private final Key key;

    public JwtAuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public JwtAuthToken createAuthToken(String id, String role, Date expiredDate) {
        return new JwtAuthToken(id, role, expiredDate, key);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {
        if (authToken.validate()) {
            Claims claims = authToken.getData();
        }
    }
}
