package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Notification;
import com.appQLCT.AppQLCT.service.core.NotificationService;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // 🟢 Lấy danh sách thông báo của user
    @GetMapping
    public List<Notification> getUserNotifications() {
        User currentUser = userService.getCurrentUser();
        return notificationService.getUserNotifications(currentUser);
    }

    // 🟡 Đánh dấu 1 thông báo đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    // ⭐ ĐÁNH DẤU TẤT CẢ ĐÃ ĐỌC ⭐
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {
        User currentUser = userService.getCurrentUser();
        notificationService.markAllAsRead(currentUser);
        return ResponseEntity.ok().build();
    }

    // 🔴 Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
