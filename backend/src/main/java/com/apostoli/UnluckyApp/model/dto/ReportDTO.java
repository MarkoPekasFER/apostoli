package com.apostoli.UnluckyApp.model.dto;

import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;
    private DisasterType disasterType;
    private Location locationName;
    private LocalDateTime reportDateTime;
    private String description;
    private ReportStatus status;
    private String username;

}
