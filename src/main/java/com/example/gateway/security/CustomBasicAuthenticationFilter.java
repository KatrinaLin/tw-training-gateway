package com.example.gateway.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;

public class CustomBasicAuthenticationFilter extends OncePerRequestFilter {


    private final AuthenticationManager authenticationManager;

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic")) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authenticate = null;
        try {
            String[] tokens = extractAndDecodeHeader(header);
            String username = tokens[0];
            Authentication authRequest = new CustomUsernamePasswordAuthenticationToken(username, tokens[1]);
            authenticate = this.authenticationManager.authenticate(authRequest);
        } catch (Exception failed) {
            failed.printStackTrace();
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        filterChain.doFilter(request, response);
    }

    private String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = URLDecoder.decode(new String(decoded, "UTF-8"), "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }

        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
