package com.apostoli.UnluckyApp.model.entity;

import com.apostoli.UnluckyApp.model.enums.OrgRank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    @JsonBackReference
    private Organisation organisation;

    private boolean verified;

    @Enumerated(EnumType.STRING)
    private OrgRank orgRank;

    @ManyToMany(fetch = FetchType.EAGER) // Fetch roles eagerly
    private List<Role> roles;

    @OneToMany
    private List<City> cities;
    //For which cities do you wish to receive alerts from?

    public AppUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", organisation=" + organisation +
                ", verified=" + verified +
                ", orgRank=" + orgRank +
                ", roles=" + roles +
                ", cities=" + cities +
                '}';
    }
}