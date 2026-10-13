package com.example.auth.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.auth.entity.AuthUsr;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "my-super-secret-key-for-authentication-123456789";

    private final long EXPIRATION_TIME = 1000 * 60 * 10; // 10 minutes

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AuthUsr authUsr) {
        return Jwts.builder()
                .subject(authUsr.getUsername())
                .claim("userId", authUsr.getUserId())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public void invalidateToken(String token) {
        if (token != null && !token.isBlank()) {
            blacklistedTokens.add(token.trim());
        }
    }

    public boolean isTokenInvalid(String token) {
        if (token == null || token.isBlank()) {
            return true;
        }
        return blacklistedTokens.contains(token.trim());
    }

    public boolean isTokenValid(String token, String username) {
        try {
            if (isTokenInvalid(token)) {
                return false;
            }

            String extractedUsername = extractUsername(token);

            return extractedUsername != null
                    && extractedUsername.equals(username)
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public String generateExpiredToken(AuthUsr authUsr) {
        return Jwts.builder()
                .subject(authUsr.getUsername())
                .claim("userId", authUsr.getUserId())
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 2))
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 30))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(AuthUsr authUsr) {
        return Jwts.builder()
                .subject(authUsr.getUsername())
                .claim("userId", authUsr.getUserId())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L * 60 * 60 * 24 * 7 // one week expiration
                        ))
                .signWith(getSigningKey())
                .compact();
    }
}