package com.safetrack.service;

import com.safetrack.model.Student;
import com.safetrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MediaService mediaService;

    @Transactional
    public Student registerStudent(
            String nim, String fullName, String email, String password,
            String faculty, String studyProgram, MultipartFile ktmPhoto) throws IOException {

        if (studentRepository.existsById(nim)) {
            throw new IllegalArgumentException("NIM sudah terdaftar: " + nim);
        }

        Student student = new Student();
        student.setNim(nim);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setFaculty(faculty);
        student.setStudyProgram(studyProgram);

        if (ktmPhoto != null && !ktmPhoto.isEmpty()) {
            String ktmUrl = mediaService.uploadFile(ktmPhoto, nim);
            student.setKtmPhotoUrl(ktmUrl);
        }

        return studentRepository.save(student);
    }

    @Transactional
    public Student verifyStudent(String nim) {
        Student student = studentRepository.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));

        student.verify();
        return studentRepository.save(student);
    }

    public Student findByNim(String nim) {
        return studentRepository.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));
    }

    // --- FUNGSI AUTENTIKASI LOGIN ---
    public Student authenticate(String nim, String rawPassword) {
        Student student = studentRepository.findById(nim)
                .orElseThrow(() -> new IllegalArgumentException("NIM tidak terdaftar di sistem."));

        if (!passwordEncoder.matches(rawPassword, student.getPassword())) {
            throw new IllegalArgumentException("Kata sandi yang Anda masukkan salah.");
        }

        return student;
    }

    /**
     * Pendaftaran Mahasiswa versi ringkas untuk integrasi UI.
     * Mengisi data wajib yang tidak ada di form dengan nilai default.
     */
    @Transactional
    public Student registerSimple(String nim, String fullName, String password) {
        if (studentRepository.existsById(nim)) {
            throw new IllegalArgumentException("NIM " + nim + " sudah terdaftar.");
        }

        Student student = new Student();
        student.setNim(nim);
        student.setFullName(fullName);
        student.setPassword(passwordEncoder.encode(password)); // Enkapsulasi password

        // Data default agar tidak error null (Sesuai skema Database PBO Anda)
        student.setEmail(nim + "@mail.usu.ac.id");
        student.setFaculty("Fasilkom-TI");
        student.setStudyProgram("Ilmu Komputer");

        return studentRepository.save(student);
    }
}