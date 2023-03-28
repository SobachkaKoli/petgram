package com.example.petgram.security.jwt;

import com.auth0.jwt.JWT;
import com.example.petgram.config.AppProperties;
import com.example.petgram.security.oauth2.CustomOAuth2User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final AppProperties appProperties;

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }


    public String createToken(Authentication authentication) {
        CustomOAuth2User userPrincipal = (CustomOAuth2User) authentication.getPrincipal();

        Claims claims = Jwts.claims().setSubject(userPrincipal.getEmail());

        claims.put("username", userPrincipal.getUsername());
        claims.put("provider", userPrincipal.getProvider());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token);


        return !claims.getBody().getExpiration().before(new Date());
    }

    public String trimToken(String token) {
        return token.substring(7);
    }



    public UserPrincipal getJwtUser(String token){
        return (UserPrincipal) userDetailsService.loadUserByUsername(JWT.decode(trimToken(token)).getSubject());
    }
}
