package com.safetrack.repository;

import com.safetrack.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    // JpaRepository sudah menyediakan method bawaan seperti save(), findById(), existsById()
    // NIM (String) digunakan sebagai Primary Key
}