package com.apostoli.UnluckyApp.security;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.RoleRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;


    public OAuth2User verifyOath2Token(String token) throws GeneralSecurityException, IOException {
        try {
            // Initialize the GoogleIdToken verifier
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
                    .setAudience(Collections.singletonList("735239155080-0k6m3afviut8pmos7lgej7guaoenic5t.apps.googleusercontent.com"))
                    .setIssuer("https://accounts.google.com")
                    .build();

            // Verify the ID token
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                // The ID token is valid, extract the payload
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user information from the payload
                String email = payload.getEmail();
                String username = (String) payload.get("name");

                Map<String, Object> attributes = payload;
                // Find or create the user in the database
                AppUser user = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            AppUser newUser = new AppUser();
                            newUser.setEmail(email);
                            newUser.setUsername(username);
                            // Add the user with the USER role
                            Role userRole = roleRepository.findByName(RoleType.USER)
                                    .orElseThrow(() -> new IllegalStateException("Role not found"));
                            List<Role> roles = new ArrayList<>();
                            roles.add(userRole);
                            roles.stream().map(role -> "ROLE_"+role.getName().name()).collect(Collectors.toList());

                            newUser.setRoles(roles);
                            newUser.setVerified(true);
                            return userRepository.save(newUser);
                        });

                List<String> roleClaims = user.getRoles().stream()
                        .map(role -> "ROLE_" + role.getName().name())
                        .collect(Collectors.toList());

                CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, attributes);
                customOAuth2User.getAttributes().put("roles", roleClaims);

                // Create and return the CustomOAuth2User
                return customOAuth2User;
            } else {
                //System.out.println("Token verification failed");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}