package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.service.impl.ReportServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportServiceImpl reportService;

    @Autowired
    public ReportController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/submit")
    public Report submitReport(@Valid @RequestBody Report report, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            return reportService.submitReport(report, username);
        } else {
            return reportService.submitReport(report, null);
        }
    }

    @GetMapping("/all")
    public List<Report> fetchALlReports() {
        return reportService.fetchAllReports();
    }



    @PostMapping("/reject/{reportId}")
    public boolean rejectReport(@PathVariable Long reportId) {
        return reportService.rejectReport(reportId);
    }

    @PostMapping("/resolve/{reportId}")
    public boolean resolveReport(@PathVariable Long reportId) {
        return reportService.resolveReport(reportId);
    }

    @PostMapping("/approve/{reportId}")
    public boolean approveReport(@PathVariable Long reportId) {
        return reportService.approveReport(reportId);
    }


}
