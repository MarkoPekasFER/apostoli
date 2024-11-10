package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserService userService;

    @Autowired
    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser user) {
        return userService.verify(user);
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "Profile information";
    }

}
