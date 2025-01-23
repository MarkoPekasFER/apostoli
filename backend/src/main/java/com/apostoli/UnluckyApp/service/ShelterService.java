package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Shelter;

import java.util.List;

public interface ShelterService {
    List<Shelter> fetchAllShelters();

    void createShelter(Shelter shelter, String username);

    void deleteShelter(String shelterName, String username);

    List<Shelter> fetchSheltersByCity(String cityName);
}
