package com.apostoli.UnluckyApp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Photo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private byte[] data; //storing the photo

    private LocalDateTime uploadedTime;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;


    public Photo(byte[] data, LocalDateTime uploadedTime, Report report) {
        this.data = data;
        this.uploadedTime = uploadedTime;
        this.report = report;
    }

}
