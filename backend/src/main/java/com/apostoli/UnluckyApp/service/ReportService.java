package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.Report;

import java.util.List;

public interface ReportService {
    Report submitReport(Report report, String username);

    List<ReportDTO> fetchAllReports();

    Report getReportById(Long reportId);

    boolean rejectReport(Long reportId);

    boolean resolveReport(Long reportId);

    boolean approveReport(Long reportId);
}
