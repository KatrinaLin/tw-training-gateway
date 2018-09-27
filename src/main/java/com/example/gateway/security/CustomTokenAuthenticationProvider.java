package com.example.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        Map<String, Object> user = jdbctemplate.queryForMap("select * from users where username = '" + username + "'");
        if (user.size() == 0)
            throw new BadCredentialsException("invalid token");
        String userId = user.get("id").toString();
        Map<String, Object> authorities = jdbctemplate.queryForMap("select * from authorities where user_id = '" + userId.toString() + "'");
        List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority(authorities.get("role").toString()));
        return new CustomTokenToken(userId, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
