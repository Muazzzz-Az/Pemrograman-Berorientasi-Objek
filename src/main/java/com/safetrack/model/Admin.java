package com.safetrack.model;

import jakarta.persistence.*;

/**
 * INHERITANCE: Admin mewarisi semua field dari abstract class User.
 * ENCAPSULATION: field isActive tidak punya setter langsung,
 *                hanya bisa diubah lewat method activate() / deactivate().
 */
@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(name = "admin_code", nullable = false, unique = true)
    private String adminCode;

    @Column(nullable = false)
    private String department;

    @Column(name = "is_active")
    private boolean isActive = true;

    // ABSTRACTION: implementasi getRole() yang diwajibkan abstract class User
    @Override
    public String getRole() {
        return "ADMIN";
    }

    // ENCAPSULATION: isActive tidak boleh di-set sembarangan dari luar
    // hanya bisa lewat dua method ini
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    // ===== Getters & Setters =====

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        if (adminCode == null || adminCode.isBlank()) {
            throw new IllegalArgumentException("Kode admin tidak boleh kosong");
        }
        this.adminCode = adminCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter tersedia, tapi TIDAK ada setIsActive() → hanya lewat activate/deactivate
    public boolean isActive() {
        return isActive;
    }
}
