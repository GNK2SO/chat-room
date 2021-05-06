package com.gnk2so.chatroom.auth.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.gnk2so.chatroom.auth.exception.InvalidCredentialsException;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AuthServiceTest {
    
    @Autowired
    private AuthService service;

    @MockBean
    private UserRepository repository;

    @Test
    public void shouldNotThrowExceptionWhenAuthenticateWithSuccess() {

        User user = UserMock.build();
        User secureUser = UserMock.buildSecuredFrom(user);
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(secureUser));

        assertDoesNotThrow(() -> {
            service.authenticate(user.getEmail(), user.getPassword());
        });
    }

    @Test
    public void shouldThrowInvalidCredentialsExceptionWhenAuthenticateWithWrongEmail() {

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            service.authenticate("user@email.com", "P@ssw0rd");
        });
    }


    @Test
    public void shouldThrowInvalidCredentialsExceptionWhenAuthenticateWithWrongPassword() {

        User user = UserMock.build();
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> {
            service.authenticate(user.getEmail(), "WrongP@ssw0rd");
        });
    }

}
