package com.apostoli.UnluckyApp.service.impl;


import com.apostoli.UnluckyApp.model.entity.Photo;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.repository.PhotoRepository;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Service
public class PhotoServiceImpl implements com.apostoli.UnluckyApp.service.PhotoService {


    private final ReportRepository reportRepository;
    private final PhotoRepository photoRepository;

    private static final int BITE_SIZE = 1024 * 4;

    @Autowired
    public PhotoServiceImpl(ReportRepository reportRepository, PhotoRepository photoRepository) {
        this.reportRepository = reportRepository;
        this.photoRepository = photoRepository;
    }


    @Override
    public void uploadPhoto(Long reportId, MultipartFile file) throws IOException {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report with id " + reportId + " not found"));


        List<Photo> album = report.getPhotos();

        Photo photoToSave = Photo.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(compressBytes(file.getBytes()))
                .uploadedTime(LocalDateTime.now())
                .report(report)
                .build();



        album.add(photoToSave);
        reportRepository.save(report);
    }

    @Transactional
    @Override
    public List<byte[]> downloadImage(Long reportId) {
        Optional<Report> reportOptional = reportRepository.findById(reportId);

        if (reportOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report with id " + reportId + " not found");
        }

        List<Photo> dbImage = reportOptional.get().getPhotos();

        if (dbImage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report with id " + reportId + " has no photos");
        }

        List<byte[]> album = new ArrayList<>();

        for (Photo photo : dbImage) {
            try {
                album.add(decompressImage(photo.getData()));
            } catch (DataFormatException | IOException exception) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading an image");
            }
        }
        return album;
    }




    public static byte[] compressBytes(byte[] data) throws IOException{
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[BITE_SIZE];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();

        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[BITE_SIZE];

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }




    @Override
    public List<Photo> getPhotosByReportId(Long reportId) {
        return photoRepository.findByReportId(reportId);
    }

    @Override
    public Photo getPhoto(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo with id " + photoId + " not found"));
    }

    @Override
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }

}
