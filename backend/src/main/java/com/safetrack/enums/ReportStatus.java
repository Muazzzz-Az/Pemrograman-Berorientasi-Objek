package com.safetrack.enums;

/**
 * Enum status laporan — digunakan oleh semua jenis Report.
 */
public enum ReportStatus {
    PENDING,      // baru dibuat, belum diproses
    PROCESSING,   // sedang ditangani admin
    RESOLVED,     // selesai ditangani
    CANCELLED     // dibatalkan oleh pelapor
}
