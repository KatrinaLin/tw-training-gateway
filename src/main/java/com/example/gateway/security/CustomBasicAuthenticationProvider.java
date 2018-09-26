package com.example.gateway.security;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomBasicAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        Map<String, Object> user = jdbctemplate.queryForMap("select * from users where username = '" + username + "'");
        String password = authentication.getCredentials().toString();
        String passwordInDB = user.get("password").toString();
        String userId = user.get("id").toString();
        Map<String, Object> authorities = jdbctemplate.queryForMap("select * from authorities where user_id = '" + userId.toString() + "'");

        if (passwordEncoder.matches(password, passwordInDB)) {
            List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(authorities.get("role").toString()));
            return new CustomUsernamePasswordAuthenticationToken(userId, roles);
        }
        throw new BadCredentialsException("invalid password");
    }

    @Override
    public boolean supports(Class<?> token) {
        return CustomUsernamePasswordAuthenticationToken.class.isAssignableFrom(token);
    }
}
