package com.apostoli.UnluckyApp.config;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class EmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // Jedinstveni token za e-poštu

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timeStamp; // Vrijeme kreiranja

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // Povezivanje s entitetom AppUser


    private boolean confirmed;

    public EmailToken(String token, LocalDateTime timeStamp, AppUser user,boolean confirmed) {
        this.token = token;
        this.timeStamp = timeStamp;
        this.user = user;
        this.confirmed=confirmed;
    }
}
