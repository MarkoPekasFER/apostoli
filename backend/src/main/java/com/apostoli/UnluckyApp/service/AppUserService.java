package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    void registerUser(AppUser user);
    List<AppUser> getAllUsers();
    Optional<AppUser> fetchUserInfoByUsername(String username);
    Optional<AppUser> fetchUserInfoByEmail(String email);
    String verify(AppUser user);
}
