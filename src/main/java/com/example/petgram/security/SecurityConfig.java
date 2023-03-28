package com.example.petgram.security;

import com.example.petgram.model.Role;

import com.example.petgram.security.jwt.JwtConfigurer;
import com.example.petgram.security.jwt.JwtTokenProvider;
import com.example.petgram.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;

import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.RestAuthenticationEntryPoint;
import com.example.petgram.security.jwt.JwtUserDetailsService;
import com.example.petgram.security.RestAuthenticationEntryPoint;
import com.example.petgram.security.oauth2.CustomOAuth2UserService;
import com.example.petgram.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.petgram.security.oauth2.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final JwtUserDetailsService jwtUserDetailsService;
    @Bean
    public CustomOAuth2UserService customOAuth2UserService(){
        return new CustomOAuth2UserService();
    }
    @Autowired
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .anyRequest().permitAll()
//                .requestMatchers("/api/login/*", "/api/user/registration").permitAll()
//                .requestMatchers("/api/auth/*", "/api/oauth2/*").permitAll()
//                .requestMatchers("/auth/*", "/oauth2/*")
//                .permitAll()
//                .requestMatchers("/api/user/*").hasAuthority(Role.USER.getAuthority())
//                .requestMatchers("/api/admin/*").hasAuthority(Role.ADMIN.getAuthority())
//                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService())
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

//        http.addFilterAfter(customAuthenticationFilter);
//        http.addFilterBefore(new CustomAuthorizationFilter(),
//                UsernamePasswordAuthenticationFilter.class).authenticationManager(authenticationManager);
        return http.build();
    }


//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
//    }

}
