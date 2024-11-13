package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {


    private final ReportRepository reportRepository;
    private final LocationService locationService;
    private final AppUserService appUserService;


    @Autowired
    public ReportService(ReportRepository reportRepository , LocationService locationService, AppUserService appUserService) {
        this.reportRepository = reportRepository;
        this.locationService = locationService;
        this.appUserService = appUserService;
    }

    public Report submitReport(Report report, String username) {

        Location location = report.getLocation();
        if (location.getId() == null) {
            location = locationService.findOrCreateLocation(location);
            report.setLocation(location);
        }
        report.setReportDateTime(LocalDateTime.now());
        report.setStatus(ReportStatus.PENDING);
        report.setUser(appUserService.fetchUserInfoByUsername(username).orElse(null));
        //TODO: add logic to set the photos
        return reportRepository.save(report);
    }
}
