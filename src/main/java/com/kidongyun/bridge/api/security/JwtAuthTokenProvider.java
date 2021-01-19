package com.kidongyun.bridge.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.toList;

@Component
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken> {
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public JwtAuthTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode("randomrandomrandomrandomrandom");
        this.key = Keys.hmacShaKeyFor(keyBytes);
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
    public Authentication getAuthentication(JwtAuthToken authToken) throws Exception {
        if (authToken.validate()) {
            Claims claims = authToken.getData();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(toList());

            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new Exception("Failed to create authentication object from JwtAuthToken");
        }
    }
}
