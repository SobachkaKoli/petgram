package com.example.petgram.security.oauth2;

import com.example.petgram.model.AuthProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
@Data
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final String email;
    private final String username;
    private final AuthProvider provider;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getName() {
        return email;
    }
}
