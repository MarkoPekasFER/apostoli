package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Report;

import com.apostoli.UnluckyApp.service.impl.AppUserServiceImpl;
import com.apostoli.UnluckyApp.service.impl.ReportServiceImpl;
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
    private final AppUserServiceImpl appUserServiceImpl;

    @Autowired
    public AppUserController(AppUserServiceImpl userService, EmailTokenService emailTokenService, AppUserServiceImpl appUserServiceImpl) {
        this.userService = userService;
        this.emailTokenService = emailTokenService;
        this.appUserServiceImpl = appUserServiceImpl;
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

    @GetMapping("/myReports")
    public List<Report> GetUserReports(Principal principal) {
        String username = principal.getName();
        AppUser user = userService.fetchUserInfoByUsername(username).orElse(null);
        return userService.fetchUserReportsByUsername(user);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        boolean isConfirmed = emailTokenService.confirmToken(token);
        if (isConfirmed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Confirmed");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
    }

    @PostMapping("/joinOrg/{orgName}")
    public ResponseEntity<String> requestToJoinOrg(Principal principal,@PathVariable String orgName) {
        appUserServiceImpl.joinOrg(principal.getName(), orgName);
        return ResponseEntity.status(HttpStatus.OK).body("Request sent");
    }

    @PostMapping("/leaveOrg/{orgName}")
    public ResponseEntity<String> requestToLeaveOrg(Principal principal,@PathVariable String orgName) {
        appUserServiceImpl.leaveOrg(principal.getName(), orgName);
        return ResponseEntity.status(HttpStatus.OK).body("Request sent");
    }


    @GetMapping("/statsByCity/{city}")
    public int getStatsByCity(@PathVariable String city) {
        return appUserServiceImpl.getStatsByCity(city);
    }
    @GetMapping("/statsByDisaster/{disasterType}")
    public int getStatsByDisaster(@PathVariable String disasterType) {
        return appUserServiceImpl.getStatsByDisasterType(disasterType);
    }
    @GetMapping("/statsByDisasterAndCity/{city}/{disasterType}")
    public int getStatsByDisaster(@PathVariable String city,@PathVariable String disasterType) {
        return appUserServiceImpl.getStatsByCityAndDisasterType(city,disasterType);
    }

    @PostMapping("/promoteUser/{username}")
    public void promoteUser(@PathVariable String username, Principal principal){
        appUserServiceImpl.promoteRole(principal.getName(),username);
    }

    @PostMapping("/demoteUser/{username}")
    public void demoteUser(@PathVariable String username, Principal principal){
        appUserServiceImpl.demoteRole(principal.getName(),username);
    }

    @PostMapping("/addCity/{city}")
    public void addCity(@PathVariable String city, Principal principal){
        appUserServiceImpl.addCity(principal.getName(),city);
    }

    @PostMapping("/removeCity/{city}")
    public void removeCity(@PathVariable String city, Principal principal){
        appUserServiceImpl.removeCity(principal.getName(),city);
    }


}
