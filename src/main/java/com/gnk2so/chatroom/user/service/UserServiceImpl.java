package com.gnk2so.chatroom.user.service;

import com.gnk2so.chatroom.user.exception.AlreadyUsedEmailException;
import com.gnk2so.chatroom.user.exception.UserNotFoundException;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        if(hasAlreadyUsedEmail(user)) {
            throw new AlreadyUsedEmailException();
        }
        return repository.save(user);
    }

    private boolean hasAlreadyUsedEmail(User user) {
        return repository.existsByEmail(user.getEmail());
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());
    }
 
}
