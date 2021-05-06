package com.gnk2so.chatroom.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gnk2so.chatroom.auth.exception.InvalidCredentialsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Override
    public void authenticate(String email, String password) {
        try {
            Authentication authentication = manager.authenticate(authenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }

    private UsernamePasswordAuthenticationToken authenticationToken(String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password, authorities());
    }

    private List<GrantedAuthority> authorities() {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("AUTH")));
    }
}
