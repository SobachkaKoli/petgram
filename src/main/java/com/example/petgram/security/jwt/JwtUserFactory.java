package com.example.petgram.security.jwt;

import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

public class JwtUserFactory {
    public static UserPrincipal create(User user){
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                mapToGrantedAuthorities(user.getRole()));
    }
    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        if (isNull(role)) return Collections.emptyList();
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
