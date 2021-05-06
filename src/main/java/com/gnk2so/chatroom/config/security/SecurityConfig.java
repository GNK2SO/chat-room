package com.gnk2so.chatroom.config.security;

import static com.gnk2so.chatroom.provider.jwt.JwtTokenRole.AUTH;
import static com.gnk2so.chatroom.provider.jwt.JwtTokenRole.REFRESH;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.gnk2so.chatroom.config.doc.SwaggerConfig;
import com.gnk2so.chatroom.provider.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService service;

    @Autowired
    private JwtProvider jwtProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilterBefore(
            new JwtFilter(jwtProvider, service), 
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
            .accessDeniedHandler(new CustomAccessHandler())
            .authenticationEntryPoint(new CustomAuthEntryPoint())
            .and()
        .authorizeRequests()
            .antMatchers(SwaggerConfig.RESOURCES).permitAll()
            .antMatchers(POST, "/auth/login").permitAll()
            .antMatchers(POST, "/users").permitAll()
            .antMatchers(GET, "/auth/refresh").hasAnyAuthority(REFRESH.getValue())
            .anyRequest().hasAnyAuthority(AUTH.getValue());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
