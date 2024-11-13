package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {

    Optional<City> findByName(String name);

    Optional<City> findByZipCode(String zipCode);

}
