package com.safetrack.service;

import com.safetrack.model.EmergencyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * POLYMORPHISM: Implementasi konkret dari NotificationService via WebSocket.
 * Bisa diganti dengan implementasi lain (misal: EmailNotification)
 * tanpa mengubah ReportService sama sekali — cukup ganti bean di config.
 *
 * ENCAPSULATION: detail koneksi WebSocket tersembunyi di dalam class ini.
 */
@Service
public class WebSocketNotification implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendAlert(EmergencyReport report) {
        // Kirim ke semua admin yang subscribe /topic/emergency
        messagingTemplate.convertAndSend("/topic/emergency", buildAlertPayload(report));
        System.out.println("[ALERT] Emergency report dikirim ke admin: reportId=" + report.getReportId());
    }

    @Override
    public void notifyAdmin(String message) {
        messagingTemplate.convertAndSend("/topic/admin", message);
    }

    @Override
    public void notifyStudent(String nim, String message) {
        // Kirim notif personal ke student tertentu
        messagingTemplate.convertAndSendToUser(nim, "/queue/notification", message);
    }

    // ENCAPSULATION: method private, hanya dipakai internal
    private String buildAlertPayload(EmergencyReport report) {
        return String.format(
            "{\"reportId\":%d, \"lat\":%.6f, \"lon\":%.6f, \"nim\":\"%s\", \"status\":\"%s\"}",
            report.getReportId(),
            report.getLatitude(),
            report.getLongitude(),
            report.getReporter().getNim(),
            report.getStatus()
        );
    }
}
