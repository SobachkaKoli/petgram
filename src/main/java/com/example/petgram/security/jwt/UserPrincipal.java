package com.example.petgram.security.jwt;


import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails{
    private final String id;
    private final String email;
    private final String username;
    private final String password;
    private final Role role;
    private final Collection<? extends GrantedAuthority> authorities;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("Authorities");
        log.info(String.valueOf(getRole()));
        return List.of(getRole());
    }

    public String  getId(){ return id;}


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
