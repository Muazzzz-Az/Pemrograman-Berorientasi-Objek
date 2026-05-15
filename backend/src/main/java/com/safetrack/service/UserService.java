package com.safetrack.service;

import com.safetrack.model.Student;
import com.safetrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service untuk registrasi dan manajemen akun mahasiswa.
 *
 * ENCAPSULATION: logika pendaftaran dan verifikasi tersembunyi di sini.
 * Controller tidak perlu tahu cara enkripsi password atau cara verifikasi KTM.
 */
@Service
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MediaService mediaService;

    /**
     * Daftarkan mahasiswa baru dengan upload foto KTM.
     */
    @Transactional
    public Student registerStudent(
            String nim,
            String fullName,
            String email,
            String password,
            String faculty,
            String studyProgram,
            MultipartFile ktmPhoto) throws IOException {

        // Cek NIM sudah terdaftar
        if (studentRepository.existsById(nim)) {
            throw new IllegalArgumentException("NIM sudah terdaftar: " + nim);
        }

        Student student = new Student();
        student.setNim(nim);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password)); // ENCAPSULATION: password di-hash
        student.setFaculty(faculty);
        student.setStudyProgram(studyProgram);

        // Upload KTM
        if (ktmPhoto != null && !ktmPhoto.isEmpty()) {
            String ktmUrl = mediaService.uploadFile(ktmPhoto, nim);
            student.setKtmPhotoUrl(ktmUrl);
        }

        return studentRepository.save(student);
    }

    /**
     * Admin memverifikasi akun mahasiswa setelah cek KTM.
     */
    @Transactional
    public Student verifyStudent(String nim) {
        Student student = studentRepository.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));

        student.verify(); // ENCAPSULATION: validasi ada di dalam Student.verify()
        return studentRepository.save(student);
    }

    /**
     * Cari student berdasarkan NIM.
     */
    public Student findByNim(String nim) {
        return studentRepository.findById(nim)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + nim));
    }
}
