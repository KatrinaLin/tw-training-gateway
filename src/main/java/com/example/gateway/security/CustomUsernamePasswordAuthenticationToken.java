package com.example.gateway.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public CustomUsernamePasswordAuthenticationToken(String username, String token) {
        super(username, token);
    }

    public CustomUsernamePasswordAuthenticationToken(String userId,
                                                     Collection<? extends GrantedAuthority> authorities) {
        super(userId, null, authorities);
    }
}
