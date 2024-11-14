package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.security.JwtService;
import com.apostoli.UnluckyApp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    private final AuthenticationManager authManager;

    private final RoleServiceImpl roleService;

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository, JwtService jwtService, AuthenticationManager authManager, RoleServiceImpl roleService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.roleService = roleService;
        this.encoder = new BCryptPasswordEncoder(13);
    }

    public void registerUser(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        roleService.createRoleIfNotFound(RoleType.USER);
        Role userRole = roleService.findByName(RoleType.USER);
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }



    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> fetchUserInfoByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<AppUser> fetchUserInfoByEmail(String username) {
        return userRepository.findByEmail(username);
    }


    public String verify(AppUser user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername(), userRepository.findByUsername(user.getUsername()).get().getRoles());
        } else {
            return "fail";
        }
    }


}
