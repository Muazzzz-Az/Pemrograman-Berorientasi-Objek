package com.safetrack.model;

import com.safetrack.enums.ReportStatus;
import jakarta.persistence.*;

/**
 * INHERITANCE: FollowUpReport mewarisi semua field dari abstract class Report.
 *
 * POLYMORPHISM: processReport() berbeda dari EmergencyReport —
 *               FollowUpReport hanya update status, tidak kirim alert baru.
 *
 * ENCAPSULATION: field tambahan (parentReportId, description) semua private.
 */
@Entity
@Table(name = "followup_reports")
public class FollowUpReport extends Report {

    @Column(name = "parent_report_id", nullable = false)
    private Long parentReportId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "additional_media_url")
    private String additionalMediaUrl;

    // POLYMORPHISM: implementasi berbeda dari EmergencyReport
    // FollowUpReport → hanya update status, tidak kirim alert baru ke admin
    @Override
    public void processReport() {
        this.setStatus(ReportStatus.PROCESSING);
        // Tidak ada alertSent di sini — tindak lanjut tidak perlu notif baru
    }

    // ===== Getters & Setters =====

    public Long getParentReportId() {
        return parentReportId;
    }

    public void setParentReportId(Long parentReportId) {
        if (parentReportId == null) {
            throw new IllegalArgumentException("ID laporan induk tidak boleh kosong");
        }
        this.parentReportId = parentReportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalMediaUrl() {
        return additionalMediaUrl;
    }

    public void setAdditionalMediaUrl(String additionalMediaUrl) {
        this.additionalMediaUrl = additionalMediaUrl;
    }
}
