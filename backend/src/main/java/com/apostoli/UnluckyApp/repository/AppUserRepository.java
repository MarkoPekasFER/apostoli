package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByCities_Name(String cityName);


}
