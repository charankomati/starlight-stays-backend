package com.starlight.user.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Same secret used by Starlight-Booking
    private static final String SECRET_KEY =
            "StarlightStaysSecretKeyForJWTAuthentication2026";

    // Token validity: 24 hours
    private static final long EXPIRATION_TIME =
            24 * 60 * 60 * 1000;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Generate JWT with user ID
    public String generateToken(
            Long userId,
            String email,
            String role) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + EXPIRATION_TIME
        );

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract email
    public String extractEmail(String token) {

        return getClaims(token)
                .getSubject();
    }

    // Extract user ID
    public Long extractUserId(String token) {

        Object userId = getClaims(token)
                .get("userId");

        if (userId == null) {
            return null;
        }

        return Long.valueOf(userId.toString());
    }

    // Extract role
    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // Validate JWT
    public boolean isTokenValid(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract claims
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}