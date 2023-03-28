//package com.example.petgram.security.filter;
//
//import com.example.petgram.security.jwt.JwtTokenProvider;
//import com.example.petgram.security.jwt.JwtUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//public class CustomAuthenticationFilter extends OncePerRequestFilter {
//
//
//    @Autowired
//    private JwtTokenProvider tokenProvider;
//
//    @Autowired
//    private JwtUserDetailsService customJwtUserDetailsService;
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String jwt = getJwtFromRequest(request);
//
//            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//                String username = tokenProvider.getUsername(jwt);
//
//                UserDetails userDetails = customJwtUserDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception ex) {
//            logger.error("Could not set user authentication in security context", ex);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
//
//
////
////
//////    @Override
//////    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//////        String username = request.getParameter("username");
//////        String password = request.getParameter("password");
//////        log.info("Username is:{}", username);
//////        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
//////        return  authenticationManager.authenticate(authenticationToken);
//////    }
//////    @Override
//////    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//////        //security_user
//////        JwtUser user = (JwtUser) authentication.getPrincipal();
//////        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//////        String access_token = JWT.create()
//////                .withSubject(user.getId())
//////                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 10000))
//////                .withIssuer(request.getRequestURL().toString())
//////                .withClaim("role",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//////                .sign(algorithm);
//////        String refresh_token = JWT.create()
//////                .withSubject(user.getUsername())
//////                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
//////                .withIssuer(request.getRequestURL().toString())
//////                .sign(algorithm);
////////        response.setHeader("access_token",access_token);
////////        response.setHeader("refresh_token",refresh_token);
//////        Map<String, String> tokens = new HashMap<>();
//////        tokens.put("access_token",access_token);
//////        tokens.put("refresh_token",refresh_token);
//////        response.setContentType(APPLICATION_JSON_VALUE);
//////
//////        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
//////    }
////
////}
