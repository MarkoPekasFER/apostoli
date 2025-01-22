package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.City;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter,Long> {

    List<Shelter> findByCity(City city);

    Optional<Shelter> findByName(String name);
}
