package com.apostoli.UnluckyApp.service.impl;


import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PhotoServiceImpl implements PhotoService {


    private final ReportRepository reportRepository;

    @Autowired
    public PhotoServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
}
