package com.example.petgram.security.oauth;


import com.example.petgram.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);
        System.out.println(user.getAttributes());
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);
        customOAuth2User.setProviderId(userRequest.getClientRegistration().getRegistrationId());
        System.out.println(customOAuth2User.getEmail());

        return customOAuth2User;
    }


}
