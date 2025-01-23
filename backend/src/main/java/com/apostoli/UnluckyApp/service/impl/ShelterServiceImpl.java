package com.apostoli.UnluckyApp.service.impl;


import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.CityRepository;
import com.apostoli.UnluckyApp.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShelterServiceImpl implements com.apostoli.UnluckyApp.service.ShelterService {

    private final ShelterRepository shelterRepository;

    private final AppUserRepository appUserRepository;

    private final CityRepository cityRepository;


    @Autowired
    public ShelterServiceImpl(ShelterRepository shelterRepository,AppUserRepository appUserRepository,CityRepository cityRepository){
        this.shelterRepository=shelterRepository;
        this.appUserRepository=appUserRepository;
        this.cityRepository=cityRepository;
    }

    @Override
    public List<Shelter> fetchAllShelters(){
        return shelterRepository.findAll();
    }

    @Override
    public void createShelter(Shelter shelter, String username){


        shelter.setName(shelter.getName());
        shelter.setCity(shelter.getCity());
        shelter.setLocation(shelter.getLocation());
        shelter.setMaxNoPeople(shelter.getMaxNoPeople());

        shelterRepository.save(shelter);

    }

    @Override
    public void deleteShelter(String shelterName, String username){

        Shelter shelter = shelterRepository.findByName(shelterName).orElse(null);

        Long shelterID = shelter.getId();

        shelterRepository.deleteById(shelterID);
    }

    @Override
    public List<Shelter> fetchSheltersByCity(String cityName) {
        City city = cityRepository.findByName(cityName).orElse(null);
        List<Shelter> filteredShelters = shelterRepository.findByCity(city);
        return filteredShelters;
    }
}
