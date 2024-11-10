package com.apostoli.UnluckyApp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne
    private City city;


    //location in a place - we have an adress
    public Location(String adress, Double latitude, Double longitude, City city) {
        this.adress = adress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    //location with no adress
    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
