package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.PhotoResponse;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.service.impl.PhotoServiceImpl;
import com.apostoli.UnluckyApp.service.impl.ReportServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportServiceImpl reportService;
    private final PhotoServiceImpl photoService;
    private final PhotoServiceImpl photoServiceImpl;

    @Autowired
    public ReportController(ReportServiceImpl reportService, PhotoServiceImpl photoService, PhotoServiceImpl photoServiceImpl) {
        this.reportService = reportService;
        this.photoService = photoService;
        this.photoServiceImpl = photoServiceImpl;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Report> submitReport(
            @RequestPart("report") String reportJson,  // Sirovi JSON
            @RequestPart(value="files", required=false) List<MultipartFile> files,
            Principal principal
    ) throws JsonProcessingException {
        // 1. Parsiraj JSON u Report objekt
        ObjectMapper objectMapper = new ObjectMapper();
        Report report = objectMapper.readValue(reportJson, Report.class);

        // 2. Ako je korisnik logiran, dodaj ga u report
        String username = (principal != null) ? principal.getName() : null;
        Report savedReport = reportService.submitReport(report, username);

        // 3. Spremi priložene slike, ako ih ima
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    photoService.uploadPhoto(savedReport.getId(), file);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }

        // 4. Vrati spremljeni report
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }


    @GetMapping("/all")
    public List<Report> fetchALlReports() {
        return reportService.fetchAllReports();
    }

    @GetMapping("/{reportId}/photos")
    public List<PhotoResponse> downloadImage(@PathVariable Long reportId) {
        List<byte[]> album = photoServiceImpl.downloadImage(reportId);
        List<PhotoResponse> response = new ArrayList<>();
        for (byte[] imageData : album) {
            response.add(new PhotoResponse(imageData));
        }
        return response;
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
