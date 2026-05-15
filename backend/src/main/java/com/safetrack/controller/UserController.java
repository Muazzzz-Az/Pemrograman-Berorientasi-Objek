package com.safetrack.controller;

import com.safetrack.model.Student;
import com.safetrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST Controller untuk registrasi dan manajemen akun.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/users/register
     * Daftar akun mahasiswa baru dengan upload KTM.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String nim,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String faculty,
            @RequestParam String studyProgram,
            @RequestParam(required = false) MultipartFile ktmPhoto) {
        try {
            Student student = userService.registerStudent(
                    nim, fullName, email, password, faculty, studyProgram, ktmPhoto);
            return ResponseEntity.ok("Registrasi berhasil. NIM: " + student.getNim());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal upload KTM: " + e.getMessage());
        }
    }

    /**
     * PUT /api/users/{nim}/verify
     * Admin memverifikasi akun mahasiswa.
     */
    @PutMapping("/{nim}/verify")
    public ResponseEntity<?> verify(@PathVariable String nim) {
        try {
            Student student = userService.verifyStudent(nim);
            return ResponseEntity.ok("Akun " + student.getFullName() + " berhasil diverifikasi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/users/{nim}
     * Ambil data mahasiswa berdasarkan NIM.
     */
    @GetMapping("/{nim}")
    public ResponseEntity<?> getStudent(@PathVariable String nim) {
        try {
            Student student = userService.findByNim(nim);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
