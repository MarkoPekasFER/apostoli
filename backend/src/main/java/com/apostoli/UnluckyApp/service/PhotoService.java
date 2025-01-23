package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.entity.Photo;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    void uploadPhoto(Long reportId, MultipartFile file) throws IOException;

    @Transactional
    List<byte[]> downloadImage(Long reportId);

    List<Photo> getPhotosByReportId(Long reportId);

    Photo getPhoto(Long photoId);

    void deletePhoto(Long photoId);
}
