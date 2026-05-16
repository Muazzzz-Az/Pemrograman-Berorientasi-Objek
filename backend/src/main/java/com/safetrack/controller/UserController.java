package com.safetrack.controller;

import com.safetrack.model.Admin;
import com.safetrack.model.Student;
import com.safetrack.service.AdminService;
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
    private final AdminService adminService;

    @Autowired
    public UserController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    public record LoginRequest(String nim, String password, String role) {}
    public record RegisterRequest(String nim, String password, String fullName, String role) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            if ("STUDENT".equals(request.role())) {
                Student student = userService.authenticate(request.nim(), request.password());
                return ResponseEntity.ok(Map.of(
                        "nim", student.getNim(),
                        "name", student.getFullName(),
                        "role", "STUDENT"
                ));
            } else {
                Admin admin = adminService.authenticate(request.nim(), request.password());
                return ResponseEntity.ok(Map.of(
                        "nim", admin.getNim(),
                        "name", admin.getFullName(),
                        "role", "ADMIN"
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Kesalahan server."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if ("STUDENT".equals(request.role())) {
                userService.registerSimple(request.nim(), request.fullName(), request.password());
            } else {
                adminService.registerSimple(request.nim(), request.fullName(), request.password());
            }
            return ResponseEntity.ok(Map.of("message", "Registrasi " + request.role() + " berhasil! Silakan Login."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Gagal mendaftarkan akun."));
        }
    }
}