package com.userscontrolsystem.userscontrolapi.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceRedis {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private RedisTemplate<String, String> redis;

    public Optional<Authentication> authenticate(String token) {
        return Optional.of(token).flatMap(this::lookup);
    }

    private Optional<Authentication> lookup(String token) {
        try {
            String userId = this.redis.opsForValue().get(token);
            if (userId != null) {
                Authentication authentication = createAuthentication(userId, Role.USER);
                return Optional.of(authentication);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Unknown error while trying to look up Redis token", e);
            return Optional.empty();
        }
    }

    /*
    private static Optional<String> extractBearerTokenHeader(@NonNull HttpServletRequest request) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null) {
                if (authorization.startsWith(BEARER_PREFIX)) {
                    String token = authorization.substring(BEARER_PREFIX.length()).trim();
                    if (!token.isBlank()) {
                        return Optional.of(token);
                    }
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("An unknown error occurred while trying to extract bearer token", e);
            return Optional.empty();
        }
    }
     */
    
    private static Authentication createAuthentication(String actor, @NonNull Role... roles) {
        // The difference between `hasAuthority` and `hasRole` is that the latter uses the `ROLE_` prefix
        List<GrantedAuthority> authorities = Stream.of(roles)
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken((actor != null) ? actor : "N/A", "N/A", authorities);
    }

    private enum Role {
        USER,
        ADMIN,
        SYSTEM,
    }
}
