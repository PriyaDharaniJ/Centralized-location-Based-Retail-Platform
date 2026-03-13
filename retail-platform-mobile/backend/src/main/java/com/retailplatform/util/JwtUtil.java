package com.retailplatform.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private Long expiration;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, String role, Long id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) { return extractClaims(token).getSubject(); }
    public String extractRole(String token) { return (String) extractClaims(token).get("role"); }
    public Long extractId(String token) {
        Object id = extractClaims(token).get("id");
        if (id instanceof Integer) return ((Integer) id).longValue();
        return (Long) id;
    }

    public boolean isTokenValid(String token) {
        try { return !extractClaims(token).getExpiration().before(new Date()); }
        catch (Exception e) { return false; }
    }
}
