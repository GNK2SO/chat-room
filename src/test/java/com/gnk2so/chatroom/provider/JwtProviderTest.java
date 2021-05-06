package com.gnk2so.chatroom.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.gnk2so.chatroom.provider.jwt.JwtProvider;
import com.gnk2so.chatroom.provider.jwt.JwtTokenRole;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtProviderTest {
    
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void shouldValidateTokenWhenCreateValidAuthToken() {
        String token = jwtProvider.createAuthToken("user@email.com");
        assertTrue(jwtProvider.isValid(token));
    }

    @Test
    public void shouldValidateTokenWhenCreateValidRefreshToken() {
        String token = jwtProvider.createRefreshToken("user@email.com");
        assertTrue(jwtProvider.isValid(token));
    }

    @Test
    public void shouldInvalidateTokenWhenCreateExpiredToken() {
        Instant expiration = Instant.now().minus(1, ChronoUnit.HOURS);
        String token = jwtProvider.createToken("user@email.com", "ROLE", expiration);
        assertFalse(jwtProvider.isValid(token));
    }

    @Test
    public void shouldInvalidateTokenWhenCreateNotValidAuthToken() {
        String token = "INVALID_TOKEN";
        assertFalse(jwtProvider.isValid(token));
    }

    @Test
    public void shouldReturnEmailUsedAsSubjectWhenGetSubject() {
        String email = "user@email.com";
        String token = jwtProvider.createAuthToken(email);
        String gettedEmail = jwtProvider.getSubject(token);
        assertEquals(gettedEmail, email);
    }

    @Test
    public void shouldReturnAuthRoleWhenGetRoleFromAuthToken() {
        String token = jwtProvider.createAuthToken("user@email.com");
        String role = jwtProvider.getRole(token);
        assertEquals(role, JwtTokenRole.AUTH.getValue());
    }

    @Test
    public void shouldReturnRefreshRoleWhenGetRoleFromRefreshToken() {
        String token = jwtProvider.createRefreshToken("user@email.com");
        String role = jwtProvider.getRole(token);
        assertEquals(role, JwtTokenRole.REFRESH.getValue());
    }
}
