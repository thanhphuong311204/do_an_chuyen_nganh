package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    // -------------------- LẤY DANH SÁCH USER --------------------
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // -------------------- LẤY USER THEO ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------- TẠO USER --------------------
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User userRequest) {
        try {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                return ResponseEntity.badRequest().body("Email đã tồn tại");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userRequest.setPasswordHash(
                    encoder.encode(userRequest.getPasswordHash())
            );

            userRequest.setActive(true);
            userRequest.setRole("USER");
            userRequest.setCreatedAt(LocalDateTime.now());

            userRepository.save(userRequest);

            return ResponseEntity.ok(userRequest);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server");
        }
    }

    // -------------------- XÓA USER --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa user");
    }
}
