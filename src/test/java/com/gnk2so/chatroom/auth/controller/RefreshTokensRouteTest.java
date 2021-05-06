package com.gnk2so.chatroom.auth.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RefreshTokensRouteTest extends ControllerTest {
    
    private final String ROUTE = "/auth/refresh";

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnStatusCodeOkAndNewsTokensWhenRefreshTokensSuccefully() throws Exception {
        
        User user = repository.save(UserMock.buildSecured());
        
        doGetRequest(ROUTE, refreshToken(user.getEmail()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.auth").isNotEmpty())
            .andExpect(jsonPath("$.auth").isString())
            .andExpect(jsonPath("$.refresh").isNotEmpty())
            .andExpect(jsonPath("$.refresh").isString());
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenSendRequestWithEmptyBearer() throws Exception {

        doGetRequest(ROUTE, "")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenSendRequestWithInvalidBearer() throws Exception {

        doGetRequest(ROUTE, "Bearer 123456")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenSendRequestWithAuthToken() throws Exception {

        User user = repository.save(UserMock.buildSecured());

        doGetRequest(ROUTE, authToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }
}
