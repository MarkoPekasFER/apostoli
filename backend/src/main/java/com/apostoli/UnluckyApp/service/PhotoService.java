package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Photo;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private ReportRepository reportRepository;



}
