package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


         reportRepository.save(report);
         return report;
    }

    public List<ReportDTO> fetchAllReports() {
            List<Report> reports = reportRepository.findAll();
            return reports.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
    }

    private ReportDTO mapToDTO(Report report) {
        return new ReportDTO(
                report.getId(),
                report.getDisasterType(),
                report.getLocation(),
                report.getReportDateTime(),
                report.getDescription(),
                report.getStatus(),
                report.getUser().getUsername()
        );
    }


    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report with id " + reportId + " not found"));
    }


    public boolean rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.FALSE);
        reportRepository.save(report);
        return true;
    }

    public boolean resolveReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        return true;
    }


    public boolean approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.ACTIVE);
        reportRepository.save(report);
        return true;
    }



}
