package com.example.petgram.controller;


import com.example.petgram.DTO.UserDto;
import com.example.petgram.Exception.BadRequestException;
import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.model.AuthProvider;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.requests.LoginRequest;
import com.example.petgram.requests.SignUpRequest;
import com.example.petgram.response.ApiResponse;
import com.example.petgram.response.AuthResponse;
import com.example.petgram.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new AuthResponse( authService.login(loginRequest)));
    }

    @PostMapping("/signup")
    public User registerUser(@RequestBody UserDto userDto) throws Status434UsernameNotUniqueException {
        return authService.signUp(userDto);
    }


}