package com.apostoli.UnluckyApp.model.entity;

import com.apostoli.UnluckyApp.model.enums.DisasterType;
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

public class Instruction {

    //safety instructions for disasters

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private DisasterType disasterType;

    @ManyToOne
    private Contact contact;


}
