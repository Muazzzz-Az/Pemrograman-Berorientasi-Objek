package com.safetrack.service;

import com.safetrack.model.Admin;
import com.safetrack.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Admin registerSimple(String nim, String fullName, String password) {
        if (adminRepository.existsById(nim)) {
            throw new IllegalArgumentException("ID Petugas " + nim + " sudah terdaftar.");
        }

        Admin admin = new Admin();
        admin.setNim(nim); // Bertindak sebagai ID Petugas
        admin.setFullName(fullName);
        admin.setPassword(passwordEncoder.encode(password));

        // Nilai default untuk memenuhi konstrain database
        admin.setEmail(nim + "@satgas.usu.ac.id");
        admin.setAdminCode("SATGAS-" + nim);
        admin.setDepartment("Satuan Keamanan Kampus");

        return adminRepository.save(admin);
    }

    public Admin authenticate(String nim, String rawPassword) {
        Admin admin = adminRepository.findById(nim)
                .orElseThrow(() -> new IllegalArgumentException("ID Petugas tidak ditemukan di sistem."));

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new IllegalArgumentException("Kata sandi Satgas salah.");
        }

        return admin;
    }
}