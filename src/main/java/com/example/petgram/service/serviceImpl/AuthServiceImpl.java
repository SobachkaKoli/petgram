package com.example.petgram.service.serviceImpl;

import com.example.petgram.DTO.*;

import com.example.petgram.Exception.Status434UsernameNotUniqueException;

import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.requests.LoginRequest;

import com.example.petgram.security.MyPasswordEncoder;
import com.example.petgram.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.io.File;


@Service
@Slf4j

public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final MyPasswordEncoder myPasswordEncoder;


    @Value("${upload.path}")
    private String uploadPath;

    public AuthServiceImpl(UserRepository userRepository, MyPasswordEncoder myPasswordEncoder) {
        this.userRepository = userRepository;
        this.myPasswordEncoder = myPasswordEncoder;
    }


    @Override
    public String login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public User signUp(UserDto userDTO) throws Status434UsernameNotUniqueException {
        if ((userRepository.existsByUsername(userDTO.getUsername())) ||  (userRepository.existsByEmail(userDTO.getEmail()))) {
            throw new Status434UsernameNotUniqueException();
        }

        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(myPasswordEncoder.passwordEncoder().encode(userDTO.getPassword()))
                .role(Role.USER)
                .build();

        File userDir = new File(uploadPath + "/" + user.getUsername());
        userDir.mkdir();
        log.info("User dir {}", userDir.getPath());

        File userAvatarsDir = new File(uploadPath + "/" + user.getUsername() + "/avatars");
        userAvatarsDir.mkdir();
        log.info("User avatars dir {}", userAvatarsDir.getPath());

        File userPostsDir = new File(uploadPath + "/" + user.getUsername() + "/posts");
        userPostsDir.mkdir();
        log.info("User posts dir {}", userPostsDir.getPath());

        log.info("Saving new user {} to the database", user.getUsername());
//        emailService.sendSimpleMessage(userDTO.getEmail(), "OK", "Test");
        log.info("user {}", user);

        return userRepository.save(user);

    }
}
