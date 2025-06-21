package com.capsule.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path imageStorageLocation = Paths.get("uploads/images").toAbsolutePath().normalize();
    private final Path videoStorageLocation = Paths.get("uploads/videos").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(imageStorageLocation);
            Files.createDirectories(videoStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directories", ex);
        }
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty or null");
        }
        return storeFile(file, imageStorageLocation, "image");
    }

    public String saveVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file is empty or null");
        }
        return storeFile(file, videoStorageLocation, "video");
    }

    private String storeFile(MultipartFile file, Path storageLocation, String type) {
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        try {
            Path targetLocation = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/" + type + "s/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
