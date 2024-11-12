package com.apostoli.UnluckyApp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Individual {

    // Personal data about registered users
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private Long OIB;

    public Individual(String email, String firstName, String lastName, Long OIB) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.OIB = OIB;
    }
}
