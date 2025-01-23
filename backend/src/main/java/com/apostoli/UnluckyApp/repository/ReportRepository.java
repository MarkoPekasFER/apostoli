package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long> {

    List<Report> findByUser(AppUser user);

    Optional<Report> findByReportDateTime(LocalDateTime dateTime);

    Optional<Report> findByLocation(Location location);

    Optional<Report> findByDisasterType(DisasterType disasterType);
}
