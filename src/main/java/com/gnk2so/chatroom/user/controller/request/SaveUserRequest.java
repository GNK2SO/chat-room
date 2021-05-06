package com.gnk2so.chatroom.user.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnk2so.chatroom.user.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveUserRequest {
    
    @NotNull
    @Size(min = 6, max = 24)
    private String name;

    @NotNull
    @Email
    @Size(max = 64, message = "size must be lower than 64")
    private String email;

    @NotNull
    @Size(min = 6, max = 24)
    private String password;
	
    @JsonIgnore
    public User getUser() {
		return User.builder()
            .name(name)
            .email(email)
            .password(encryptedPassword())
            .build();
	}
    
    private String encryptedPassword() {
        return new BCryptPasswordEncoder().encode(password);
    }
}
