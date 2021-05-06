package com.gnk2so.chatroom.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.gnk2so.chatroom.user.exception.AlreadyUsedEmailException;
import com.gnk2so.chatroom.user.exception.UserNotFoundException;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    public void shouldReturnUserWithSameDataWhenSaveUserWithSuccess() 
    {
        User user = UserMock.build();
        when(repository.save(any(User.class))).thenReturn(UserMock.withIdFrom(user));

        User savedUser = service.save(user);

        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }


    @Test
    public void shouldReturnUserWithNotNullIdWhenSaveUserWithSuccess() 
    {
        User user = UserMock.build();
        when(repository.save(any(User.class))).thenReturn(UserMock.withIdFrom(user));

        User savedUser = service.save(user);

        assertNotNull(savedUser.getId());
    }

    @Test
    public void shouldThrowsAlreadyUsedEmailExceptionWhenSaveUserWithAlreadyUsedEmail() 
    {
        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(AlreadyUsedEmailException.class, () -> {
            service.save(UserMock.build());
        });
    }


    @Test
    public void shouldReturnUserWhenFindUserByEmail() 
    {
        User user = UserMock.build();
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User gettedUsed = service.findByEmail(user.getEmail());

        assertEquals(gettedUsed, user);
    }

    @Test
    public void shouldThrowsUserNotFoundExceptionWhenNotFindUserByEmail() 
    {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            service.findByEmail("mocked.user@email.com");
        });
    }

    @Test
    public void shouldReturnUserWhenFindUserById() 
    {
        User user = UserMock.buildWithId();
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        User gettedUsed = service.findById(user.getId());

        assertEquals(gettedUsed, user);
    }


    @Test
    public void shouldThrowsUserNotFoundExceptionWhenNotFindUserById() 
    {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            service.findById(1L);
        });
    }
    
}