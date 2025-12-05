package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.enu.AuthProvider;
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

    // -------------------- LẤY DANH SÁCH --------------------
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // -------------------- XEM CHI TIẾT --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------- KHÓA USER --------------------
    @PutMapping("/{id}/ban")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    return ResponseEntity.ok("Đã khóa tài khoản");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------- MỞ KHÓA USER --------------------
    @PutMapping("/{id}/unban")
    public ResponseEntity<?> unbanUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(true);
                    userRepository.save(user);
                    return ResponseEntity.ok("Đã mở khóa tài khoản");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // -------------------- THÊM NGƯỜI DÙNG MỚI --------------------
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User userRequest) {
        try {
            // 1️⃣ Kiểm tra email trùng
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                return ResponseEntity.badRequest().body("Email đã tồn tại!");
            }

            // 2️⃣ Nếu không nhập username → tự sinh từ email
            if (userRequest.getUsername() == null || userRequest.getUsername().isEmpty()) {
                userRequest.setUsername(userRequest.getEmail().split("@")[0]);
            }

            // ✅ 3️⃣ Mã hóa mật khẩu (nếu nhập), nếu không → mặc định "123456"
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (userRequest.getPasswordHash() == null || userRequest.getPasswordHash().isEmpty()) {
                userRequest.setPasswordHash(encoder.encode("123456"));
            } else {
                userRequest.setPasswordHash(encoder.encode(userRequest.getPasswordHash()));
            }

            // 4️⃣ Các thông tin mặc định
            userRequest.setActive(true);
            userRequest.setBiometricEnable(0);
            userRequest.setRole("USER");
            userRequest.setAuthProvider(AuthProvider.email);
            userRequest.setCreatedAt(LocalDateTime.now());

            // 5️⃣ Lưu vào DB
            userRepository.save(userRequest);

            return ResponseEntity.ok("Tạo người dùng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
}
