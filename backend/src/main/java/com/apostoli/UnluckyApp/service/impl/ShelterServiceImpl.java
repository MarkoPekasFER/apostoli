package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Role;
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
public class ShelterServiceImpl {

    private final ShelterRepository shelterRepository;

    private final AppUserRepository appUserRepository;

    private final CityRepository cityRepository;


    @Autowired
    public ShelterServiceImpl(ShelterRepository shelterRepository,AppUserRepository appUserRepository,CityRepository cityRepository){
        this.shelterRepository=shelterRepository;
        this.appUserRepository=appUserRepository;
        this.cityRepository=cityRepository;
    }

    public List<Shelter> fetchAllShelters(){
        return shelterRepository.findAll();
    }

    public void createShelter(Shelter shelter,String username){

        AppUser user = appUserRepository.findByUsername(username).orElse(null);

        List<RoleType> allowedRoles = new ArrayList<>();

        allowedRoles.add(RoleType.SUPER_ADMIN);
        allowedRoles.add(RoleType.ADMIN);
        allowedRoles.add(RoleType.RESPONDER);
        allowedRoles.add(RoleType.ORGANISATION);


        boolean allowed=false;

        List<Role> roles = user.getRoles();

         for(int i=0;i<roles.size();i++){
             if(allowedRoles.contains(roles.get(i)))
                 allowed=true;
         }

        if(!allowed){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

        shelter.setName(shelter.getName());
        shelter.setCity(shelter.getCity());
        shelter.setLocation(shelter.getLocation());
        shelter.setMaxNoPeople(shelter.getMaxNoPeople());

        shelterRepository.save(shelter);

    }

    public void deleteShelter(String shelterName,String username){

        Shelter shelter = shelterRepository.findByName(shelterName).orElse(null);

        Long shelterID = shelter.getId();

        if(!shelterRepository.existsById(shelterID))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Shelter not found");


        AppUser user = appUserRepository.findByUsername(username).orElse(null);

        List<RoleType> allowedRoles = new ArrayList<>();

        allowedRoles.add(RoleType.SUPER_ADMIN);
        allowedRoles.add(RoleType.ADMIN);
        allowedRoles.add(RoleType.RESPONDER);
        allowedRoles.add(RoleType.ORGANISATION);

        boolean allowed=false;

        List<Role> roles = user.getRoles();

        for(int i=0;i<roles.size();i++){
            if(allowedRoles.contains(roles.get(i)))
                allowed=true;
        }

        if(!allowed){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this resource.");
        }

        shelterRepository.deleteById(shelterID);
    }

    public List<Shelter> fetchSheltersByCity(String cityName) {
        City city = cityRepository.findByName(cityName).orElse(null);
        List<Shelter> filteredShelters = shelterRepository.findByCity(city);
        return filteredShelters;
    }
}
