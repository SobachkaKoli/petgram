package com.example.petgram.service.serviceImpl;

import com.example.petgram.DTO.*;
import com.example.petgram.Exception.BadRequestException;
import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.model.AuthProvider;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.jwt.JwtTokenProvider;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserConverter userConverter;




    @Override
    @Transactional
    public User register(RegistrationDto registrationDto, String language) {
        if (isNull(registrationDto.getRole()))
            throw new BadRequestException("UserRole should not be null");

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = userRepository.save(User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(registrationDto.getRole())
                .provider(AuthProvider.local)
                .build());

        return user;
    }

    @Override
    public User register(OAuth2UserDto registrationDto, String language) {
        Claims claims = jwtTokenProvider.getClaims(registrationDto.getToken());

        if (isNull(registrationDto.getRole()))
            throw new BadRequestException("UserRole should not be null");

        if (userRepository.existsByEmail(claims.getSubject())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = userRepository.save(User.builder()
                .username(claims.get("username", String.class))
                .email(claims.getSubject())
                .role(registrationDto.getRole())
                .provider(AuthProvider.valueOf(claims.get("provider", String.class)))
                .build());

        return user;
    }

    @Override
    public SuccessfulAuthResponse login(LoginDto loginDto) throws Status430UserNotFoundException {
        String username = loginDto.getEmail();

        if(!userRepository.existsByEmail(username))
            throw new Status430UserNotFoundException(username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));

        return buildSuccessfulAuthResponse(username);
    }

    @Override
    public SuccessfulAuthResponse login(String token) throws Status430UserNotFoundException {
        Claims claims = jwtTokenProvider.getClaims(token);
        String username = claims.getSubject();

        return buildSuccessfulAuthResponse(username);
    }

    private SuccessfulAuthResponse buildSuccessfulAuthResponse(String username) throws Status430UserNotFoundException {
        UserDto userDto = userConverter.convertToDto(userRepository.findByEmail(username)
                .orElseThrow(() -> new Status430UserNotFoundException(username)));
        String token = jwtTokenProvider.createToken(username);
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);

        return SuccessfulAuthResponse.builder()
                .accessToken(token)
                .user(userDto)
                .build();
    }
    @Override
    public void logout(UserPrincipal userPrincipal) throws Status430UserNotFoundException {
        String email = userPrincipal.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Status430UserNotFoundException(email));
//        refreshTokenService.deleteByUserId(user.getId());
    }

}
