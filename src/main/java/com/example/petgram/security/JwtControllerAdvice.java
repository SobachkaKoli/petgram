package com.example.petgram.security;

import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class JwtControllerAdvice {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @ModelAttribute
    public JwtUser getJwtUser(WebRequest request){
        String token = request.getHeader("Authorization");
        if (token!=null) return jwtTokenProvider.getJwtUser(token);
        else return null;
    }

}
