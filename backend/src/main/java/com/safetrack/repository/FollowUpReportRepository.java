package com.safetrack.repository;

import com.safetrack.model.FollowUpReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpReportRepository extends JpaRepository<FollowUpReport, Long> {
    List<FollowUpReport> findByParentReportId(Long parentReportId);
    List<FollowUpReport> findByReporter_Nim(String nim);
}
