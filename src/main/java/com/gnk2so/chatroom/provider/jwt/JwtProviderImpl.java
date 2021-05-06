package com.gnk2so.chatroom.provider.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProviderImpl implements JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String createAuthToken(String subject) {
        String role = JwtTokenRole.AUTH.getValue();
        Instant expiration = Instant.now().plus(24, ChronoUnit.HOURS);
        return createToken(subject, role, expiration);
    }

    @Override
    public String createRefreshToken(String subject) {
        String role = JwtTokenRole.REFRESH.getValue();
        Instant expiration = Instant.now().plus(72, ChronoUnit.HOURS);
        return createToken(subject, role, expiration);
    }

    @Override
    public String createToken(String subject, String role, Instant expiration) {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(expiration))
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .claim("ROLE", role)
            .compact();
    }

    private  SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public Boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parse(token);
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public String getSubject(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    @Override
    public String getRole(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("ROLE", String.class);
    }
    
}
