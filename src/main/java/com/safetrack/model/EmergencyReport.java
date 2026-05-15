package com.safetrack.model;

import com.safetrack.enums.ReportStatus;
import jakarta.persistence.*;

/**
 * INHERITANCE: EmergencyReport mewarisi semua field dari abstract class Report
 *              (reportId, latitude, longitude, status, timestamp, reporter).
 *
 * POLYMORPHISM: mengimplementasikan processReport() dengan logika khusus
 *               laporan darurat — kirim alert langsung ke admin.
 *
 * ENCAPSULATION: field tambahan spesifik emergency (mediaUrl, mediaType, alertSent) semua private.
 */
@Entity
@Table(name = "emergency_reports")
public class EmergencyReport extends Report {

    @Column(name = "media_url")
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType = MediaType.NONE;

    @Column(name = "alert_sent")
    private boolean alertSent = false;

    @Column(name = "incident_description", columnDefinition = "TEXT")
    private String incidentDescription;

    // POLYMORPHISM: implementasi berbeda dari FollowUpReport
    // EmergencyReport → langsung set PROCESSING dan tandai alertSent = true
    @Override
    public void processReport() {
        this.setStatus(ReportStatus.PROCESSING);
        this.alertSent = true;
        // NotificationService.sendAlert(this) akan dipanggil dari ReportService
    }

    public enum MediaType {
        AUDIO,
        VIDEO,
        NONE
    }

    // ===== Getters & Setters =====

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isAlertSent() {
        return alertSent;
    }

    // ENCAPSULATION: alertSent hanya bisa di-set via processReport(), bukan setter bebas
    void setAlertSent(boolean alertSent) {
        this.alertSent = alertSent;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }
}
