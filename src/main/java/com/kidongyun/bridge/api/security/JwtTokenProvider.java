package com.kidongyun.bridge.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {
    @Value("${security.jwt.secret.key}")
    private String secretKey;
    @Value("${security.jwt.token.time}")
    private long tokenValidTime;

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get("roles").toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(toList());

        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    @Override
    public String getUsername(String token) {
        return this.getClaims(token).getSubject();
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    @Override
    public boolean isValid(String token) {
        try {
            return !this.getClaims(token).getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }
}
