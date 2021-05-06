package com.gnk2so.chatroom.provider.jwt;

import java.time.Instant;

public interface JwtProvider {
    String createAuthToken(String subject);
    String createRefreshToken(String subject);
    String createToken(String subject, String role, Instant expiration);
    Boolean isValid(String token);
	String getSubject(String token);
	String getRole(String token);
}
