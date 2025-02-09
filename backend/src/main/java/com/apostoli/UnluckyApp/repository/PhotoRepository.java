package com.apostoli.UnluckyApp.repository;

import com.apostoli.UnluckyApp.model.entity.Photo;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    List<Photo> findByReportId(Long reportId);
}
