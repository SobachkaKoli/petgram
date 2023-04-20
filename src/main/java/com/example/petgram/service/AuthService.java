package com.example.petgram.service;

import com.example.petgram.DTO.*;
import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.model.User;
import com.example.petgram.requests.LoginRequest;



public interface AuthService {
    String login(LoginRequest loginRequest);

    User signUp(UserDto userDto) throws Status434UsernameNotUniqueException;



}
