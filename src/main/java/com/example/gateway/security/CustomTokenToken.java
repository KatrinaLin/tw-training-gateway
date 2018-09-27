package com.example.gateway.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomTokenToken extends AbstractAuthenticationToken {
    private final String principal;
    private final String credentials;

    public CustomTokenToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = username;
        this.credentials = null;
        super.setAuthenticated(true);
    }

    public CustomTokenToken(String principal) {
        super(null);
        this.principal = principal;
        this.credentials = null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
