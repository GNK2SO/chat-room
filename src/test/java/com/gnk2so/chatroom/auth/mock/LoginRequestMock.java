package com.gnk2so.chatroom.auth.mock;

import com.gnk2so.chatroom.auth.controller.request.LoginRequest;

public class LoginRequestMock {

	public static LoginRequest build(String email, String password) {
		return LoginRequest.builder()
            .email(email)
            .password(password)
            .build();
	}
    
}
