package com.capsule.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path imageStorageLocation;
    private final Path videoStorageLocation;

    public FileStorageService() {
        this.imageStorageLocation = Paths.get("uploads/images").toAbsolutePath().normalize();
        this.videoStorageLocation = Paths.get("uploads/videos").toAbsolutePath().normalize();

        try {
            Files.createDirectories(imageStorageLocation);
            Files.createDirectories(videoStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directories", ex);
        }
    }

    public String saveImage(MultipartFile file) {
        return saveFile(file, imageStorageLocation, "images");
    }

    public String saveVideo(MultipartFile file) {
        return saveFile(file, videoStorageLocation, "videos");
    }

    private String saveFile(MultipartFile file, Path storageLocation, String folderName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Provided file is null or empty");
        }

        String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        try {
            Path targetPath = storageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/" + folderName + "/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + uniqueFileName, ex);
        }
    }
}
