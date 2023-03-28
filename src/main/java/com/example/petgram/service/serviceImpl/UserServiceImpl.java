package com.example.petgram.service.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.petgram.DTO.UserDto;
import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status434UserNicknameNotUniqueException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.mail.EmailServiceImpl;
import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import com.example.petgram.notifications.Notification;
import com.example.petgram.repository.NotificationRepository;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailServiceImpl emailService, NotificationRepository notificationRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserDto userDTO) throws Status434UserNicknameNotUniqueException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Status434UserNicknameNotUniqueException();
        }

        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
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

    @Override
    public User getByUsername(String username) throws Status444UserIsNull {
        return userRepository.findByUsername(username).orElseThrow(() -> new Status444UserIsNull(username + " Not found"));
    }


    @Override
    public User getAuthenticatedUser(UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException {
        return userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new Status430UserNotFoundException(userPrincipal.getId()));
    }

    @Override
    public User deleteMyAccount(UserPrincipal userPrincipal) throws Exception {

        User user = getAuthenticatedUser(userPrincipal);

        File avatar = new File(user.getAvatar());
        avatar.delete();
        user.setUsername("DELETED");
        user.setAvatar("");
        user.setEmail("");
        user.setRole(Role.DELETED);
        user.setPassword("");
        user.setFollowers(0);
        user.setFollowing(0);
        return userRepository.save(user);
    }

    @Override
    public List<Notification> getMyNotifications(UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException {
        return notificationRepository.findAllByRecipient(getAuthenticatedUser(userPrincipal));
    }



    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching users");
        return userRepository.findAll();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = getByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
//                        .withClaim("roles",user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //  response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public DecodedJWT decodedJWT(String token) {
        Base64.getDecoder().decode(JWT.decode(token).getSubject());
        JWT.decode(token);
        return JWT.decode(token);
    }
}
