package com.apostoli.UnluckyApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = removeBearer(request);

        if (token != null && !token.isEmpty()) {
            try {
                if (isJwtToken(token) && !isOAuth2Token(token)) {
                    handleJwtToken(token, request);
                } else if (isOAuth2Token(token)) {
                    handleOAuth2Token(token, request);
                }
            } catch (GeneralSecurityException e) {
                if (isJwtToken(token)) {
                    handleJwtToken(token, request);
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isJwtToken(String token) {
        // Provjeri ima li token 3 dijela (klasičan JWT format)
        return token.split("\\.").length == 3;
    }

    private boolean isOAuth2Token(String token) {
        return token.startsWith("eyJ") && token.contains("."); // Provedba osnovne provjere za Google ID token
    }

    private void handleJwtToken(String token, HttpServletRequest request) {
        String username = jwtService.extractUsernameFromToken(token);

        if (username != null && jwtService.validateToken(token)) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private void handleOAuth2Token(String token, HttpServletRequest request) throws GeneralSecurityException, IOException {
        OAuth2User oAuth2User = customOAuth2UserService.verifyOath2Token(token);
        if (oAuth2User != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(oAuth2User, null, oAuth2User.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }else {
            throw new GeneralSecurityException("OAuth2 token verification failed");
        }
    }

    private String removeBearer(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
