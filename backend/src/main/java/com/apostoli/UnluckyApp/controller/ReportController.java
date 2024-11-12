package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.Report;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {


    @PostMapping("/submit")
    private Report submitReport(@RequestBody Report report){
        return report;
    }
}
