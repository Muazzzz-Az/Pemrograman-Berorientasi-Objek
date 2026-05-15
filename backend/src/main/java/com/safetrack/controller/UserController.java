package com.safetrack.controller;

import com.safetrack.model.Student;
import com.safetrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public record LoginRequest(String nim, String password, String role) {}
    public record RegisterRequest(String nim, String password, String fullName, String role) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            if ("STUDENT".equals(request.role())) {
                // Memanggil logika dari UserService
                Student student = userService.authenticate(request.nim(), request.password());

                return ResponseEntity.ok(Map.of(
                        "nim", student.getNim(),
                        "name", student.getFullName(),
                        "role", "STUDENT"
                ));
            } else {
                // Logika Darurat Sementara untuk Admin/Satgas
                if ("admin123".equals(request.password())) {
                    return ResponseEntity.ok(Map.of(
                            "nim", request.nim(),
                            "name", "Pusat Komando Satgas",
                            "role", "ADMIN"
                    ));
                }
                throw new IllegalArgumentException("Kredensial Admin tidak valid.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Terjadi kesalahan pada server."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Memanggil logika penyimpanan asli ke Database H2
            userService.registerSimple(request.nim(), request.fullName(), request.password());
            return ResponseEntity.ok(Map.of("message", "Registrasi berhasil! Silakan Login."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Gagal mendaftarkan akun."));
        }
    }
}