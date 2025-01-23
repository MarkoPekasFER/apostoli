package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.dto.AppUserDTO;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Report;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    void registerUser(AppUser user);

    void joinOrg(String orgName, String username);

    void leaveOrg(String orgName, String username);

    List<AppUserDTO> getAllUsers();

    Optional<AppUser> fetchUserInfoByUsername(String username);

    Optional<AppUser> fetchUserInfoByEmail(String username);

    List<Report> fetchUserReportsByUsername(AppUser user);

    String verify(AppUser user);

    int getStatsByCity(String city);

    int getStatsByDisasterType(String disasterType);

    int getStatsByCityAndDisasterType(String city, String disasterType);

    void promoteRole(String superior, String target);

    void demoteRole(String superior, String target);

    void addCity(String name, String city);

    void removeCity(String name, String city);
}
