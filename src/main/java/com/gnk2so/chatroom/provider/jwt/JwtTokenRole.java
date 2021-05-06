package com.gnk2so.chatroom.provider.jwt;

import lombok.Getter;

public enum JwtTokenRole {
    AUTH("ROLE_AUTH"), REFRESH("ROLE_REFRESH");

    @Getter
    private String value;

    private JwtTokenRole(String value) {
        this.value = value;
    }
}
