package com.apostoli.UnluckyApp.model.dto;

import com.apostoli.UnluckyApp.model.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportWithPhotoResponse {
    private ReportDTO reportDTO;
    private List<PhotoResponse> photoResponse;

    public ReportWithPhotoResponse(Report report, List<PhotoResponse> photos) {
        // Mapiranje Report entiteta u ReportDTO
        this.reportDTO = new ReportDTO(
                report.getId(),
                report.getDisasterType(),
                report.getLocation(),
                report.getReportDateTime(),
                report.getDescription(),
                report.getStatus(),
                report.getUser().getUsername()
        );
        this.photoResponse = photos;
    }

}
