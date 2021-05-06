package com.gnk2so.chatroom.user.mock;

import com.gnk2so.chatroom.user.controller.request.SaveUserRequest;

public class SaveUserRequestMock {
    
    public static SaveUserRequest build() {
        return SaveUserRequest.builder()
            .name("New User")
            .email("new.user@email.com")
            .password("p@ssw0rd")
            .build();
    }

}
