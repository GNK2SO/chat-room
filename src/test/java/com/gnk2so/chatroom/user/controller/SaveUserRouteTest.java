package com.gnk2so.chatroom.user.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.user.controller.request.SaveUserRequest;
import com.gnk2so.chatroom.user.exception.AlreadyUsedEmailException;
import com.gnk2so.chatroom.user.mock.SaveUserRequestMock;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class SaveUserRouteTest extends ControllerTest {

    private final String ROUTE = "/users";

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnStatusCodeCreatedWhenSaveUserWithSuccess() throws Exception {
        doPostRequest(ROUTE, SaveUserRequestMock.build())
            .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNullName() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name(null)
            .email("new.user@email.com")
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNameLessThan6Caracteres() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("Four")
            .email("new.user@email.com")
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNameGreaterThan24Characters() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("ThisNameIsGreaterThenTwentyFourCharacters")
            .email("new.user@email.com")
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNullEmail() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email(null)
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNotValidEmail() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email("new.user@")
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must be a well-formed email address"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithEmailGreaterThan24Characters() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email("ThisEmailHasLengthGreaterThanSixtyFourCharacters.gnk2so@email.com.br")
            .password("p@ssw0rd")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be lower than 64"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithNullPassword() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email("new.user@email.com")
            .password(null)
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithPasswordLessThan6Caracteres() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email("new.user@email.com")
            .password("Four")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestAndErrorResponseWhenSendRequestWithPasswordGreaterThan24Characters() throws Exception {

        SaveUserRequest content = SaveUserRequest.builder()
            .name("New User")
            .email("new.user@email.com")
            .password("ThisPasswordIsGreaterThenTwentyFourCharacters")
            .build();

        doPostRequest(ROUTE, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeConflictAndErrorResponseWhenSendRequestWithAlreadyUsedEmail() throws Exception {
        
        SaveUserRequest content = SaveUserRequestMock.build();
        repository.save(content.getUser());
        
        doPostRequest(ROUTE, content)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(CONFLICT.value()))
            .andExpect(jsonPath("$.message").value(AlreadyUsedEmailException.MESSAGE));
    }
}
