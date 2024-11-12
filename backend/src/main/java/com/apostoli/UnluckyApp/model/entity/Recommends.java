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

public class Recommends {

    // Stores recommendations of instructions for contacts.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int contactId;
}
