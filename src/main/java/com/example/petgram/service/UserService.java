package com.example.petgram.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.petgram.DTO.UserDto;
import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.notifications.Notification;
import com.example.petgram.security.jwt.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
//    User registerUser(UserDto userDTO) throws Status434UsernameNotUniqueException;

    User findByUsername(String username) throws Status444UserIsNull;
    User getAuthenticatedUser(UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException;

    User deleteMyAccount(UserPrincipal userPrincipal) throws Exception;

    List<Notification> getMyNotifications(UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException;

    User save(User user);

    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> getUsers();

    void  refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


    boolean existsByUsername(String username);
    boolean existByEmail(String email);

    DecodedJWT decodedJWT(String token);

}
