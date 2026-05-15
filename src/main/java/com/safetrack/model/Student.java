package com.safetrack.model;

import jakarta.persistence.*;

/**
 * INHERITANCE: Student mewarisi semua field dari User (nim, email, password, dll).
 * ENCAPSULATION: field tambahan (faculty, studyProgram, isVerified) dibuat private.
 * ABSTRACTION: mengimplementasikan getRole() dari abstract class User,
 *              dan mengimplementasikan semua method dari interface Reportable.
 */
@Entity
@Table(name = "students")
public class Student extends User implements Reportable {

    // ENCAPSULATION: field tambahan spesifik Student, semua private
    @Column(nullable = false)
    private String faculty;

    @Column(name = "study_program", nullable = false)
    private String studyProgram;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "active_report_id")
    private Long activeReportId;

    // ABSTRACTION: implementasi getRole() yang diwajibkan abstract class User
    @Override
    public String getRole() {
        return "STUDENT";
    }

    // ABSTRACTION: implementasi method dari interface Reportable
    @Override
    public void submitReport() {
        if (!this.isVerified) {
            throw new IllegalStateException("Akun belum terverifikasi. Upload KTM terlebih dahulu.");
        }
        if (this.activeReportId != null) {
            throw new IllegalStateException("Kamu masih memiliki laporan aktif.");
        }
    }

    @Override
    public String getReportStatus() {
        if (activeReportId == null) {
            return "Tidak ada laporan aktif";
        }
        return "Laporan aktif dengan ID: " + activeReportId;
    }

    @Override
    public void cancelReport() {
        this.activeReportId = null;
    }

    // ===== Getters & Setters =====

    public String getFaculty() {
        return faculty;
    }

    // ENCAPSULATION: setter dengan validasi
    public void setFaculty(String faculty) {
        if (faculty == null || faculty.isBlank()) {
            throw new IllegalArgumentException("Fakultas tidak boleh kosong");
        }
        this.faculty = faculty;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(String studyProgram) {
        if (studyProgram == null || studyProgram.isBlank()) {
            throw new IllegalArgumentException("Program studi tidak boleh kosong");
        }
        this.studyProgram = studyProgram;
    }

    public boolean isVerified() {
        return isVerified;
    }

    // ENCAPSULATION: isVerified hanya bisa diubah lewat method verify(), bukan setter langsung
    public void verify() {
        if (!this.isProfileComplete()) {
            throw new IllegalStateException("Foto KTM belum diupload");
        }
        this.isVerified = true;
    }

    public Long getActiveReportId() {
        return activeReportId;
    }

    public void setActiveReportId(Long activeReportId) {
        this.activeReportId = activeReportId;
    }
}
