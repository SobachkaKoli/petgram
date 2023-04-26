package com.example.petgram.model;

import org.springframework.security.core.GrantedAuthority;

//  TODO (Bogdan O.) 26/4/23: rename to UserRole
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
