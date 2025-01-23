package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.City;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public interface CityService {

    List<City> getAllCities();
}
