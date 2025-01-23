package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements com.apostoli.UnluckyApp.service.ReportService {


    private final ReportRepository reportRepository;
    private final LocationServiceImpl locationService;
    private final AppUserServiceImpl appUserService;


    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository , LocationServiceImpl locationService, AppUserServiceImpl appUserService) {
        this.reportRepository = reportRepository;
        this.locationService = locationService;
        this.appUserService = appUserService;
    }

    @Override
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

    @Override
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


    @Override
    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report with id " + reportId + " not found"));
    }


    @Override
    public boolean rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.FALSE);
        reportRepository.save(report);
        return true;
    }

    @Override
    public boolean resolveReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        return true;
    }


    @Override
    @Transactional
    public boolean approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            return false;
        }
        report.setStatus(ReportStatus.ACTIVE);
        reportRepository.save(report);

        appUserService.notifyUsersByCity(report.getLocation().getCity().getName(), report.getDisasterType().toString(), reportId);
        return true;
    }



}
