package edu.senacsp.health_management.service;

import edu.senacsp.health_management.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;
    private final long rememberExpiration;

    public JwtService(@Value("${jwt.key}") String secret,
                      @Value("${jwt.expiration-ms}") long expiration,
                      @Value("${jwt.remember-expiration-ms}") long rememberExpiration)
    {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
        this.rememberExpiration = rememberExpiration;
    }

    public String generateToken(User user, boolean rememberMe)
    {
        Date now = new Date();

        long expirationTime = rememberMe ? rememberExpiration : expiration;

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(key)
                .compact();
    }

    public Long extractUserId(String token) {
        Number id = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Number.class);
        return id.longValue();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}