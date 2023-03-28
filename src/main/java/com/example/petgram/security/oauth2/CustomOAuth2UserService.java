package com.example.petgram.security.oauth2;

import com.example.petgram.Exception.OAuth2AuthenticationProcessingException;
import com.example.petgram.model.AuthProvider;
import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.jwt.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(oAuth2UserInfo.getName())
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .build();

        return OAuth2UserFactory.create(user, oAuth2User.getAttributes());

    }

//    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
//        User user = new User();
//        log.info("Register_____________");
//        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
//        user.setProviderId(oAuth2UserInfo.getId());
//        user.setUsername(oAuth2UserInfo.getName());
//        user.setEmail(oAuth2UserInfo.getEmail());
//        user.setAvatar(oAuth2UserInfo.getImageUrl());
//        user.setRole(Role.USER);
//        return userRepository.save(user);
//    }
//
//    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
//        existingUser.setUsername(oAuth2UserInfo.getName());
//        existingUser.setAvatar(oAuth2UserInfo.getImageUrl());
//        return userRepository.save(existingUser);
//    }


}
