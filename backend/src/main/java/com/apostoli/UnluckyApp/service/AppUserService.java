package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    private final AuthenticationManager authManager;


    @Autowired
    public AppUserService(AppUserRepository userRepository, JwtService jwtService, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.encoder = new BCryptPasswordEncoder(13);
    }

    public AppUser registerUser(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }



    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }


    public String verify(AppUser user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }
}
