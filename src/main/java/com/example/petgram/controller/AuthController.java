package com.example.petgram.controller;


import com.example.petgram.DTO.UserDto;
import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.model.User;
import com.example.petgram.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;


    //  TODO (Bogdan O.) 26/4/23: remove constructor or use @Autowired right on it
    public AuthController(AuthService authService) {
        this.authService = authService;

    }



    @PostMapping("/signup")
    public User registerUser(@RequestBody UserDto userDto) throws Status434UsernameNotUniqueException {
        return authService.signUp(userDto);
    }


}