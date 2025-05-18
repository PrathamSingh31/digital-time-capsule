package com.capsule.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // Your secret key must be at least 256 bits (32 bytes)
    private static final String SECRET_KEY = "your_very_long_secret_key_must_be_at_least_256_bits_long_1234567890";

    // Token expiration time (1 day)
    private static final long EXPIRATION_TIME = 86400000; // 24 * 60 * 60 * 1000 ms

    // Generate a signing key from the secret string
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate JWT token with username as subject
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    // Extract username from token
    public static String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Validate token expiration and signature
    public static boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid token
            return false;
        }
    }

    // Helper method to parse claims from token
    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
