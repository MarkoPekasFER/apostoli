package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.service.impl.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserServiceImpl userService;
    private final EmailTokenService emailTokenService;

    @Autowired
    public AppUserController(AppUserServiceImpl userService, EmailTokenService emailTokenService) {
        this.userService = userService;
        this.emailTokenService = emailTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        Optional<AppUser> existingUserByUsername = userService.fetchUserInfoByUsername(user.getUsername());
        Optional<AppUser> existingUserByEmail = userService.fetchUserInfoByEmail(user.getEmail());

        if (existingUserByUsername.isPresent() && existingUserByEmail.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username and Email already in use");
        }
        if (existingUserByUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
        }
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be empty");
        }

        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser user) {
        return userService.verify(user);
    }

    @GetMapping("/profile")
    public Optional<AppUser> getUserProfile(Principal principal) {
        String username = principal.getName();
        return userService.fetchUserInfoByUsername(username);
    }

    @GetMapping("/reports")
    public List<Report> GetUserReports(Principal principal) {
        String username = principal.getName();
        AppUser user = userService.fetchUserInfoByUsername(username).orElse(null);
        return userService.fetchUserReportsByUsername(user);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        boolean isConfirmed = emailTokenService.confirmToken(token);
        if (isConfirmed) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://apostoli.markopekas.com")
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
    }





}
