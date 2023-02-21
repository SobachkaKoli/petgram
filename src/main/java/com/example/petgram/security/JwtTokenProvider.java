package com.example.petgram.security;

import com.auth0.jwt.JWT;
import com.example.petgram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    @Autowired
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }
    public JwtUser getJwtUser(String token){
        return (JwtUser) userDetailsService.loadUserByUsername(JWT.decode(userService.trimToken(token)).getSubject());
    }
}
