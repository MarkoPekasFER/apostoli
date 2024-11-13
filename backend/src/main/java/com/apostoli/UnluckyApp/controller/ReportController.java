package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/submit")
    private Report submitReport(@Valid @RequestBody Report report, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            return reportService.submitReport(report, username);
        } else {
            return reportService.submitReport(report, null);
        }
    }

}
