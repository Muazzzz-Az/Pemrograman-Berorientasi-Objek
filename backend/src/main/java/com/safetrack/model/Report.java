package com.safetrack.model;

import com.safetrack.enums.ReportStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ABSTRACTION: Abstract class untuk semua jenis laporan.
 * processReport() harus diimplementasikan oleh subclass masing-masing.
 *
 * ENCAPSULATION: semua field private, diakses lewat getter/setter.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nim", nullable = false)
    private Student reporter;

    // ABSTRACTION: setiap jenis laporan punya cara pemrosesan berbeda
    public abstract void processReport();

    // Concrete method yang bisa dipakai semua subclass
    public boolean isActive() {
        return status == ReportStatus.PENDING || status == ReportStatus.PROCESSING;
    }

    public void markResolved() {
        this.status = ReportStatus.RESOLVED;
    }

    public void markCancelled() {
        this.status = ReportStatus.CANCELLED;
    }

    // ===== Getters & Setters =====

    public Long getReportId() {
        return reportId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        if (latitude == null || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude tidak valid");
        }
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        if (longitude == null || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude tidak valid");
        }
        this.longitude = longitude;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Student getReporter() {
        return reporter;
    }

    public void setReporter(Student reporter) {
        this.reporter = reporter;
    }
}
