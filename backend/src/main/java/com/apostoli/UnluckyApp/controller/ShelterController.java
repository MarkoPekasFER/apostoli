package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import com.apostoli.UnluckyApp.service.impl.ShelterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/shelter")
public class ShelterController {

    public final ShelterServiceImpl shelterService;

    @Autowired
    public ShelterController(ShelterServiceImpl shelterService){
        this.shelterService=shelterService;
    }

    @PostMapping("/create")
    public void createShelter(@RequestBody Shelter shelter, Principal principal){
        shelterService.createShelter(shelter,principal.getName());
    }

    @PostMapping("/delete/{shelterName}")
    public void deleteShelter(Principal principal, @PathVariable String shelterName){
        shelterService.deleteShelter(shelterName,principal.getName());
    }

    @PostMapping("/{cityName}")
    public List<Shelter> getSheltersByCityName(@PathVariable String cityName){
        return shelterService.fetchSheltersByCity(cityName);
    }

    @PostMapping("/all")
    public List<Shelter> getAllShelters(){ return shelterService.fetchAllShelters(); }
}
