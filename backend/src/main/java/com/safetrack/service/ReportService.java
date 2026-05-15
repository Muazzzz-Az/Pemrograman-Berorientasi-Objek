package com.safetrack.service;

import com.safetrack.model.*;
import com.safetrack.repository.EmergencyReportRepository;
import com.safetrack.repository.FollowUpReportRepository;
import com.safetrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service utama untuk mengelola laporan.
 *
 * POLYMORPHISM: method handleReport(Report) menerima tipe Report apapun.
 * Java secara otomatis memanggil processReport() yang tepat saat runtime
 * (EmergencyReport.processReport() atau FollowUpReport.processReport()).
 *
 * ENCAPSULATION: logika bisnis tersembunyi di dalam service ini,
 * controller tidak perlu tahu detail prosesnya.
 */
@Service
public class ReportService {

    @Autowired
    private EmergencyReportRepository emergencyRepo;

    @Autowired
    private FollowUpReportRepository followUpRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private NotificationService notificationService;  // POLYMORPHISM via interface

    @Autowired
    private MediaService mediaService;

    /**
     * POLYMORPHISM: method ini tidak peduli apakah report adalah
     * EmergencyReport atau FollowUpReport — Java pilih implementasi sendiri.
     */
    public void handleReport(Report report) {
        report.processReport();  // ← POLYMORPHISM terjadi di sini

        // Setelah process, cek tipe untuk aksi tambahan
        if (report instanceof EmergencyReport er) {
            notificationService.sendAlert(er);
        }
    }

    /**
     * Buat laporan darurat baru (dari tombol merah).
     */
    @Transactional
    public EmergencyReport createEmergencyReport(
            String nim,
            Double latitude,
            Double longitude,
            String description,
            MultipartFile mediaFile) throws IOException {

        Student student = studentRepo.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));

        // Panggil submitReport() dari interface Reportable — validasi terpusat
        student.submitReport();

        EmergencyReport report = new EmergencyReport();
        report.setReporter(student);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setIncidentDescription(description);

        // Upload media jika ada
        if (mediaFile != null && !mediaFile.isEmpty()) {
            String url = mediaService.uploadFile(mediaFile, nim);
            report.setMediaUrl(url);

            String contentType = mediaFile.getContentType();
            if (contentType != null && contentType.startsWith("audio")) {
                report.setMediaType(EmergencyReport.MediaType.AUDIO);
            } else if (contentType != null && contentType.startsWith("video")) {
                report.setMediaType(EmergencyReport.MediaType.VIDEO);
            }
        }

        // POLYMORPHISM: handleReport memanggil EmergencyReport.processReport()
        handleReport(report);

        EmergencyReport saved = emergencyRepo.save(report);

        // Update activeReportId di student
        student.setActiveReportId(saved.getReportId());
        studentRepo.save(student);

        return saved;
    }

    /**
     * Buat laporan tindak lanjut.
     */
    @Transactional
    public FollowUpReport createFollowUpReport(
            String nim,
            Long parentReportId,
            String description,
            MultipartFile additionalMedia) throws IOException {

        Student student = studentRepo.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));

        student.submitReport();

        FollowUpReport report = new FollowUpReport();
        report.setReporter(student);
        report.setParentReportId(parentReportId);
        report.setDescription(description);

        // Ambil koordinat dari laporan induk
        EmergencyReport parent = emergencyRepo.findById(parentReportId)
                .orElseThrow(() -> new RuntimeException("Laporan induk tidak ditemukan"));
        report.setLatitude(parent.getLatitude());
        report.setLongitude(parent.getLongitude());

        if (additionalMedia != null && !additionalMedia.isEmpty()) {
            String url = mediaService.uploadFile(additionalMedia, nim);
            report.setAdditionalMediaUrl(url);
        }

        // POLYMORPHISM: handleReport memanggil FollowUpReport.processReport()
        // (berbeda dari EmergencyReport — tidak kirim alert baru)
        handleReport(report);

        return followUpRepo.save(report);
    }

    /**
     * Admin menyelesaikan laporan.
     */
    @Transactional
    public void resolveReport(Long reportId, boolean isEmergency) {
        if (isEmergency) {
            EmergencyReport report = emergencyRepo.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Laporan tidak ditemukan"));
            report.markResolved();
            report.getReporter().cancelReport();  // bersihkan activeReportId
            emergencyRepo.save(report);
            notificationService.notifyStudent(
                report.getReporter().getNim(),
                "Laporan kamu sudah diselesaikan oleh admin."
            );
        } else {
            FollowUpReport report = followUpRepo.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Laporan tidak ditemukan"));
            report.markResolved();
            followUpRepo.save(report);
        }
    }
}
