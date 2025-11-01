package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.AdminNotificationRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.service.core.NotificationService;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/lock")
    public ResponseEntity<?> lockUser(@PathVariable Long id, @RequestParam boolean lock) {
        User user = userService.toggleUserLock(id, lock);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/notifications")
    public ResponseEntity<String> sendSystemNotification(@RequestBody AdminNotificationRequest req) {
        List<User> allUsers = userService.getAllUsers();

        for (User u : allUsers) {
            notificationService.createNotification(
                u,
                req.getTitle(),
                req.getMessage(),
                "system"
            );
        }

        return ResponseEntity.ok("Gửi thông báo cho toàn bộ người dùng thành công ✅");
    }
}
