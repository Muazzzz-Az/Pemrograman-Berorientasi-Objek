package com.safetrack.controller;

import com.safetrack.model.EmergencyReport;
import com.safetrack.model.FollowUpReport;
import com.safetrack.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST Controller untuk endpoint laporan.
 * ENCAPSULATION: controller tidak tahu detail proses — semua delegasi ke ReportService.
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173") // Wajib untuk komunikasi antar port Vite dan Spring Boot
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * POST /api/reports/emergency
     * Endpoint tombol merah — buat laporan darurat.
     */
    @PostMapping("/emergency")
    public ResponseEntity<?> createEmergencyReport(
            @RequestParam("nim") String nim,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "media", required = false) MultipartFile media) {
        try {
            EmergencyReport report = reportService.createEmergencyReport(
                    nim, latitude, longitude, description, media);
            return ResponseEntity.ok(report);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal upload media: " + e.getMessage());
        }
    }

    /**
     * POST /api/reports/followup
     * Buat laporan tindak lanjut.
     */
    @PostMapping("/followup")
    public ResponseEntity<?> createFollowUpReport(
            @RequestParam("nim") String nim,
            @RequestParam("parentReportId") Long parentReportId,
            @RequestParam("description") String description,
            @RequestParam(value = "media", required = false) MultipartFile media) {
        try {
            FollowUpReport report = reportService.createFollowUpReport(
                    nim, parentReportId, description, media);
            return ResponseEntity.ok(report);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal upload media: " + e.getMessage());
        }
    }

    /**
     * PUT /api/reports/{id}/resolve
     * Admin menyelesaikan laporan.
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolveReport(
            @PathVariable("id") Long id,
            @RequestParam("isEmergency") boolean isEmergency) {
        try {
            reportService.resolveReport(id, isEmergency);
            return ResponseEntity.ok("Laporan berhasil diselesaikan");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}