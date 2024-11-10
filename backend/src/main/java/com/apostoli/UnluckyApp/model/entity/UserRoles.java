package com.apostoli.UnluckyApp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserRoles {

    // Users and their roles.

    @JoinColumn(name = "role_id")
    private Long id;
    @JoinColumn(name = "email")
    private String email;

}
