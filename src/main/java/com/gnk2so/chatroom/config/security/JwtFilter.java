package com.gnk2so.chatroom.config.security;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gnk2so.chatroom.provider.jwt.JwtProvider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> jwt =  getJwt(request);
        if(jwt.isPresent() && isValid(jwt.get())) {
            try {
                authenticate(jwt.get());
            } catch ( Exception e ) {
                filterChain.doFilter(request, response);    
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwt(HttpServletRequest request) {
        String authentication = request.getHeader("Authorization");
        if(isNotBlank(authentication) && authentication.startsWith("Bearer ")) {
            return Optional.of(authentication.substring(7));
        }
        return Optional.empty();
    }
    
    private boolean isValid(String token) {
        return jwtProvider.isValid(token);
    }
    
    private void authenticate(String jwt) {
        String email = jwtProvider.getSubject(jwt);
        String role = jwtProvider.getRole(jwt);
        UserDetails user = userDetailsService.loadUserByUsername(email);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken(user, role));
    }

    private Authentication authenticationToken(UserDetails user, String role) {
        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities(role));
    }

    private List<GrantedAuthority> authorities(String role) {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority(role)));
    }

}
