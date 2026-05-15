package com.safetrack.service;

import com.safetrack.model.EmergencyReport;

/**
 * ABSTRACTION: Interface mendefinisikan kontrak notifikasi.
 * Implementasi konkretnya (WebSocket, Email, dll) tersembunyi dari ReportService.
 *
 * POLYMORPHISM: ReportService cukup pakai interface ini,
 *               implementasinya bisa diganti tanpa ubah ReportService sama sekali.
 */
public interface NotificationService {

    void sendAlert(EmergencyReport report);

    void notifyAdmin(String message);

    void notifyStudent(String nim, String message);
}
