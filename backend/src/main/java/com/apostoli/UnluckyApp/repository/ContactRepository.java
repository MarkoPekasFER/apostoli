package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.Contact;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,Long> {

}
