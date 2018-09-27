package com.example.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Base64;

@RestController
public class TokenController {

    @PostMapping("/tokens")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createToken(Principal principal) {

        return ResponseEntity.ok().body(Base64.getEncoder().encode(principal.getName().getBytes()));
    }
}
