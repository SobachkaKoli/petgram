package com.example.petgram.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.petgram.DTO.UserDTO;
import com.example.petgram.Exception.Status434UserNicknameNotUniqueException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.notifications.Notification;
import com.example.petgram.security.JwtUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User registerUser(UserDTO userDTO) throws Status434UserNicknameNotUniqueException;

    User getByUserName(String username) throws Status444UserIsNull;
    User getAuthenticatedUser(JwtUser jwtUser) throws Status444UserIsNull;

    User deleteMyAccount(JwtUser jwtUser) throws Exception;

    List<Notification> getMyNotifications(JwtUser jwtUser) throws Status444UserIsNull;
    String trimToken(String token);

    User save(User user);

    List<User> getUsers();

    void  refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


    boolean existsByUsername(String username);

    DecodedJWT decodedJWT(String token);

}
