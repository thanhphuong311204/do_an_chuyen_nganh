package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WalletRepository walletRepository;

    /**
     * 📄 Lấy thông tin hồ sơ + thống kê ví
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        User user = userService.getCurrentUser();

        // Lấy danh sách ví và tính tổng số dư
        List<Wallet> wallets = walletRepository.findByUser(user);
        int walletCount = wallets.size();

        BigDecimal totalBalance = wallets.stream()
                .map(Wallet::getBalance)
                .filter(b -> b != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Trả JSON về cho Flutter
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("username", user.getUsername());
        response.put("fullName", user.getFullName());
        response.put("phone", user.getPhone());
        response.put("avatarUrl", user.getAvatarUrl());
        response.put("createdAt", user.getCreatedAt());
        response.put("walletCount", walletCount);
        response.put("totalBalance", totalBalance);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, String> body) {
        User updated = userService.updateProfile(
                body.get("fullName"),
                body.get("phone"),
                body.get("avatarUrl")
        );
        return ResponseEntity.ok(updated);
    }

    /**
     * 🔐 Đổi mật khẩu
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        try {
            userService.changePassword(oldPassword, newPassword);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Đổi mật khẩu thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
