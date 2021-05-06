package com.gnk2so.chatroom.auth.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    
    @Email
    @NotNull
    @Size(max = 24, message = "size must be lower than 64")
    private String email;

    @NotNull
    @Size(min = 6, max = 24)
    private String password;

}