package com.safetrack.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ABSTRACTION: Abstract class sebagai base untuk Student dan Admin.
 * Menyembunyikan detail implementasi getRole() dari luar.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class User {

    // ENCAPSULATION: semua field private, akses lewat getter/setter
    @Id
    @Column(name = "nim", nullable = false, unique = true, length = 20)
    private String nim;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "ktm_photo_url")
    private String ktmPhotoUrl;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ABSTRACTION: method abstract, subclass wajib implementasikan
    public abstract String getRole();

    // Concrete method yang bisa dipakai semua subclass
    public boolean isProfileComplete() {
        return ktmPhotoUrl != null && !ktmPhotoUrl.isEmpty();
    }

    // ===== Getters & Setters =====

    public String getNim() {
        return nim;
    }

    // ENCAPSULATION: validasi di dalam setter
    public void setNim(String nim) {
        if (nim == null || nim.isBlank()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong");
        }
        this.nim = nim;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKtmPhotoUrl() {
        return ktmPhotoUrl;
    }

    public void setKtmPhotoUrl(String ktmPhotoUrl) {
        this.ktmPhotoUrl = ktmPhotoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
