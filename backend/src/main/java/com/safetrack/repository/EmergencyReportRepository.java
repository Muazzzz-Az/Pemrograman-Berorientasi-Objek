package com.safetrack.repository;

import com.safetrack.enums.ReportStatus;
import com.safetrack.model.EmergencyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyReportRepository extends JpaRepository<EmergencyReport, Long> {
    List<EmergencyReport> findByReporter_Nim(String nim);
    List<EmergencyReport> findByStatus(ReportStatus status);
    List<EmergencyReport> findByAlertSentFalse();
}
