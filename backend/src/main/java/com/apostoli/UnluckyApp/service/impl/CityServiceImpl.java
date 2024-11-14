package com.apostoli.UnluckyApp.service.impl;


import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.repository.CityRepository;
import com.apostoli.UnluckyApp.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private List<City> cities = new ArrayList<>();


    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        loadCities();
    }

    private void loadCities() {
        try{
            ClassPathResource resource = new ClassPathResource("CroatianCities.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String line;

            reader.readLine(); // skip the header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double latitude = Double.parseDouble(parts[2]);
                double longitude = Double.parseDouble(parts[3]);
                City city = new City(name, latitude, longitude);
                cities.add(city);
                cityRepository.save(city);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<City> getAllCities() {
        return cities;
    }


}
