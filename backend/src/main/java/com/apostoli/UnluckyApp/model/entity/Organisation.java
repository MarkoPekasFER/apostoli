package com.apostoli.UnluckyApp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    private String description;

    @OneToMany
    private List<AppUser> members;

    @OneToMany
    private List<AppUser> pendingMembers;

    public Organisation(String name, String email, List<AppUser> members) {
        this.name = name;
        this.email = email;
        this.members = members;
    }

    public Organisation(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
