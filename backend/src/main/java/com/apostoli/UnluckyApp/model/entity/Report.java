package com.apostoli.UnluckyApp.model.entity;

import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    //type of disaster,location,time,description
    //user who reported it
    //photos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DisasterType disasterType;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private LocalDateTime reportDateTime;

    @Size(max = 500)
    private String description;

    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AppUser user;

    @OneToMany(mappedBy = "report",cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();


    public Report(Long id, DisasterType disasterType, Location location, LocalDateTime reportTime, String description, AppUser user, List<Photo> photos) {
        this.id = id;
        this.disasterType = disasterType;
        this.location = location;
        this.reportDateTime = reportTime;
        this.description = description;
        this.user = user;
        this.photos = photos;
    }

    public Report(Long id, DisasterType disasterType, Location location, LocalDateTime reportTime, String description, List<Photo> photos) {
        this.id = id;
        this.disasterType = disasterType;
        this.location = location;
        this.reportDateTime = reportTime;
        this.description = description;
        this.photos = photos;
    }


}
