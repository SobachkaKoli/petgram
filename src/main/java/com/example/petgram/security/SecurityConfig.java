package com.example.petgram.security;


import com.example.petgram.Exception.Status434UsernameNotUniqueException;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.filter.CustomAuthenticationFilter;
import com.example.petgram.security.filter.CustomAuthorizationFilter;


import com.example.petgram.security.jwt.JwtUserDetailsService;


import com.example.petgram.security.oauth.CustomOAuth2User;
import com.example.petgram.security.oauth.OAuthTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserRepository userRepository;


    private final OAuthTokenProvider oAuthTokenProvider;


    @Bean
    public UserDetailsService userDetailsService(){
        return new JwtUserDetailsService((userRepository));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(myPasswordEncoder.passwordEncoder());
        return  authProvider;
    }
    @Autowired
   public MyPasswordEncoder myPasswordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(myPasswordEncoder.passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http
                .cors().and()
                .authorizeHttpRequests().anyRequest().permitAll()
//                .requestMatchers("/api/user/oauth").permitAll()
//                .requestMatchers("/api/login/**", "/api/user/registration","/").permitAll()
//                .requestMatchers("/oauth/**","/oauth2/**").permitAll()
//                .requestMatchers("/api/user/**").hasAuthority(Role.USER.getAuthority())
//                .requestMatchers("/api/admin/**").hasAuthority(Role.ADMIN.getAuthority())
//                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler((request, response, authentication) -> {

                    response.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("token", oAuthTokenProvider.createToken(oauthUser));
                    response.getOutputStream().print(objectMapper.writeValueAsString(tokenMap));

                });

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager);

        return http.build();
    }
}
