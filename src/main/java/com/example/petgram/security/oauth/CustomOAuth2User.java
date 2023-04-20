package com.example.petgram.security.oauth;



import com.example.petgram.model.Role;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oauth2User;
    private static final Role role = Role.USER;

    private String providerId;
    public CustomOAuth2User(OAuth2User oAuth2User){
        this.oauth2User = oAuth2User;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getUsername() {
        return oauth2User.getAttribute("username");
    }

    public String getEmail(){
        return oauth2User.getAttribute("email");
    }

    public String getEmails(){
        return oauth2User.getAttribute("emails");
    }

    public String getPicture(){return oauth2User.getAttribute("picture");}

    public String getLogin(){ return oauth2User.getAttribute("login");}

    public Role getRole(){ return role;}


}