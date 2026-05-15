package com.safetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * ENCAPSULATION: detail penyimpanan file tersembunyi di dalam class ini.
 * ReportService tidak perlu tahu di mana file disimpan —
 * cukup panggil uploadFile() dan dapat URL-nya.
 */
@Service
public class MediaService {

    @Value("${safetrack.upload-dir:uploads/}")
    private String uploadDir;

    @Value("${safetrack.base-url:http://localhost:8080}")
    private String baseUrl;

    public String uploadFile(MultipartFile file, String nim) throws IOException {
        validateFile(file);

        Path uploadPath = Paths.get(uploadDir, nim);
        Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        String extension = getExtension(originalName);
        String filename = UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return baseUrl + "/media/" + nim + "/" + filename;
    }

    public void deleteFile(String fileUrl) throws IOException {
        String relativePath = fileUrl.replace(baseUrl + "/media/", "");
        Path filePath = Paths.get(uploadDir, relativePath);
        Files.deleteIfExists(filePath);
    }

    // ENCAPSULATION: validasi file disembunyikan di method private
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File tidak boleh kosong");
        }
        if (file.getSize() > 50 * 1024 * 1024) { // max 50MB
            throw new IllegalArgumentException("Ukuran file maksimal 50MB");
        }
        String contentType = file.getContentType();
        if (contentType == null ||
            (!contentType.startsWith("audio") &&
             !contentType.startsWith("video") &&
             !contentType.startsWith("image"))) {
            throw new IllegalArgumentException("Format file tidak didukung");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}
