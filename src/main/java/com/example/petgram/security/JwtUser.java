package com.example.petgram.security;

import com.example.petgram.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {
    private final User user;
    public JwtUser(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole());
    }

//          user.get().getRoles().forEach(role -> listRoles.add(role.name()));
//        grantedAuthorityList = listRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    public String  getId(){ return user.getId();}

    public User getUser(){
        return this.user;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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
