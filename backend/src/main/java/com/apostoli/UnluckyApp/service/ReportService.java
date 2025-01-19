package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Report;

import java.util.List;

public interface ReportService {
    Report submitReport(Report report, String username);
    List<Report> fetchAllReports();


}
