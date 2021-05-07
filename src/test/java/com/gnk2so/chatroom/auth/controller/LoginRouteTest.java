package com.gnk2so.chatroom.auth.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.auth.controller.request.LoginRequest;
import com.gnk2so.chatroom.auth.exception.InvalidCredentialsException;
import com.gnk2so.chatroom.auth.mock.LoginRequestMock;
import com.gnk2so.chatroom.provider.jwt.JwtProvider;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class LoginRouteTest extends ControllerTest {

    private User user;
    
    private final String ROUTE = "/auth/login";

    @Autowired
    private UserRepository repository;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        user = UserMock.build();
        repository.deleteAll();
    }
    

    @Test
    public void shouldReturnStatusCodeOkAndTokensWhenAuthenticateSuccessfully() throws Exception {

        LoginRequest content = LoginRequestMock.build(user.getEmail(), user.getPassword());

        repository.save(UserMock.buildSecuredFrom(user));
        when(jwtProvider.createAuthToken(anyString())).thenReturn("AUTH");
        when(jwtProvider.createRefreshToken(anyString())).thenReturn("REFRESH");

        doPostRequest(ROUTE, content)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.auth").value("AUTH"))
            .andExpect(jsonPath("$.refresh").value("REFRESH"));

    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithNullEmail() throws Exception {

        LoginRequest content = LoginRequestMock.build(null, user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithInvalidEmail() throws Exception {

        String bigEmail = "ThisEmailHasLengthGreaterThanSixtyFourCharacters.gnk2so@email.com.br";
        LoginRequest content = LoginRequestMock.build(bigEmail, user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be lower than 64"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithNullPassword() throws Exception {

        LoginRequest content = LoginRequestMock.build(user.getEmail(), null);

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithPasswordWithLenghtLowerThanSix() throws Exception {

        LoginRequest content = LoginRequestMock.build(user.getEmail(), "FOUR");

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithPasswordWithLenghtGreaterThanTwentyFour() throws Exception {

        String bigPassword = "ThisPasswordIsGreaterThenTwentyFourCharacters";
        LoginRequest content = LoginRequestMock.build(user.getEmail(), bigPassword);

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErroMessageWhenReceiveRequestWithEmailLengthGreaterThanSixtyFour() throws Exception {

        LoginRequest content = LoginRequestMock.build("invalid.email@", user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must be a well-formed email address"));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedAndErroMessageWhenAuthenticationFailure() throws Exception {

        LoginRequest content = LoginRequestMock.build(user.getEmail(), user.getPassword());

        doPostRequest(ROUTE, content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(InvalidCredentialsException.MESSAGE));
    }
}
