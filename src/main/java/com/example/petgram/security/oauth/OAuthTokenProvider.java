package com.example.petgram.security.oauth;

import com.example.petgram.DTO.UserDto;
import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.model.AuthProvider;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthTokenProvider {

    @Value("${secret.key}")
    private String secret;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthService authService;


    public String createToken(CustomOAuth2User customOAuth2User) throws Status434UsernameNotUniqueException {

        System.out.println(customOAuth2User);
        System.out.println(customOAuth2User.getEmail());


        System.out.println("CREATING TOKEN");

        if (Boolean.TRUE.equals(userRepository.existsByEmail(customOAuth2User.getEmail()))){

            User user = userRepository.findByEmail(customOAuth2User.getEmail()).orElseThrow();
            Claims claims = Jwts.claims().setSubject(user.getEmail());
            claims.put("roles", user.getRole());
            claims.put("name", user.getUsername());
            claims.put("emails",customOAuth2User.getEmails());
            Date now = new Date(System.currentTimeMillis());

            return Jwts.builder()
                    .setSubject(customOAuth2User.getEmail())
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + 100*60*1000))
                    .setIssuedAt(now)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();

        }else{

            System.out.println(customOAuth2User);
            System.out.println(customOAuth2User.getEmail());

            UserDto userDto = new UserDto(
                    customOAuth2User.getEmail(),
                    customOAuth2User.getName(),
                    UUID.randomUUID().toString()
            );

            authService.signUp(userDto);
            Claims claims = Jwts.claims().setSubject(customOAuth2User.getName());
            claims.put("roles", customOAuth2User.getRole());
            claims.put("email", customOAuth2User.getEmail());
            Date now = new Date(System.currentTimeMillis());




            return Jwts.builder()
                    .setSubject(customOAuth2User.getEmail())
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + 100*60*1000))
                    .setIssuedAt(now)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        }
    }

//
//    public String createGithubToken(CustomOAuth2User customOAuth2User) throws Status434UsernameNotUniqueException {
//        System.out.println("CREATING TOKEN");
//
//        if ((userRepository.existsByUsername(customOAuth2User.getUsername()))){
//
//            User user = userRepository.findByUsername(customOAuth2User.getUsername()).orElseThrow();
//
//            Claims claims = Jwts.claims().setSubject(user.getUsername());
//            claims.put("roles", user.getRole());
//            claims.put("email", user.getEmail());
//            Date now = new Date(System.currentTimeMillis());
//
//            return Jwts.builder()
//                    .setClaims(claims)
//                    .setExpiration(new Date(System.currentTimeMillis() + 100*60*1000))
//                    .setIssuedAt(now)
//                    .signWith(SignatureAlgorithm.HS256, secret)
//                    .compact();
//
//        }else{
//
//            UserDto userDto = new UserDto(
//                    customOAuth2User.getEmail(),
//                    customOAuth2User.getName(),
//                    UUID.randomUUID().toString()
//            );
//
//            authService.signUp(userDto);
//            Claims claims = Jwts.claims().setSubject(customOAuth2User.getName());
//            claims.put("roles", customOAuth2User.getRole());
//            claims.put("email", customOAuth2User.getEmail());
//            Date now = new Date(System.currentTimeMillis());
//
//            return Jwts.builder()
//                    .setClaims(claims)
//                    .setExpiration(new Date(System.currentTimeMillis() + 100*60*1000))
//                    .setIssuedAt(now)
//                    .signWith(SignatureAlgorithm.HS256, secret)
//                    .compact();
//        }
//
//    }




}
