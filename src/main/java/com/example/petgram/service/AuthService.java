package com.example.petgram.service;

import com.example.petgram.DTO.LoginDto;
import com.example.petgram.DTO.OAuth2UserDto;
import com.example.petgram.DTO.RegistrationDto;
import com.example.petgram.DTO.SuccessfulAuthResponse;
import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.model.User;
import com.example.petgram.security.jwt.UserPrincipal;


public interface AuthService {
    User register(RegistrationDto registrationDto, String lang);

    User register(OAuth2UserDto registrationDto, String language);

    SuccessfulAuthResponse login(LoginDto loginDto) throws Status430UserNotFoundException;

    SuccessfulAuthResponse login(String token) throws Status430UserNotFoundException;

    void logout(UserPrincipal userPrincipal) throws Status430UserNotFoundException;
}
