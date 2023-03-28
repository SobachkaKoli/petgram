package com.example.petgram.security.oauth2;

import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class OAuth2UserFactory {

    public static CustomOAuth2User create(User user, Map<String, Object> attributes) {
        CustomOAuth2User oAuth2User = create(user);
        oAuth2User.setAttributes(attributes);
        return oAuth2User;
    }

    public static CustomOAuth2User create(User user){
        return new CustomOAuth2User(
                user.getEmail(),
                user.getUsername(),
                user.getProvider(),
                mapToGrantedAuthorities(user.getRole())
        );
    }
    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        if (isNull(role)) return Collections.emptyList();
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
