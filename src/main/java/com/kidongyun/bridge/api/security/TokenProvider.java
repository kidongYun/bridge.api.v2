package com.kidongyun.bridge.api.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public interface TokenProvider {
    String createToken(String email, List<String> roles);

    Authentication getAuthentication(String token);

    String getUsername(String token);

    Claims getClaims(String token);

    String resolveToken(HttpServletRequest request);

    boolean isValid(String token);
}
