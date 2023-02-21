package com.example.petgram.model;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER,
    DELETED,
    BANNED_USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
