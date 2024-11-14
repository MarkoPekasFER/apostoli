package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {


    private final ReportRepository reportRepository;
    private final LocationServiceImpl locationService;
    private final AppUserServiceImpl appUserService;


    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository , LocationServiceImpl locationService, AppUserServiceImpl appUserService) {
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

    public List<Report> fetchAllReports() {
        return reportRepository.findAll();
    }

}
