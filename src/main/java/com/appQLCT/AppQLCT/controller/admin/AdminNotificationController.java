package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.entity.core.Notification;
import com.appQLCT.AppQLCT.entity.core.NotificationType;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.NotificationRepository;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Notification> getSystemNotifications() {
        List<Notification> all = notificationRepository.findByTypeOrderByCreatedAtDesc(NotificationType.SYSTEM);

        return all.stream()
                .collect(Collectors.toMap(
                        n -> n.getNotificationTitle() + "|" + n.getNotificationMessage(),
                        n -> n,
                        (oldVal, newVal) -> oldVal))
                .values()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    @PostMapping("/broadcast")
    public String broadcast(@RequestBody Notification req) {
        List<User> users = userRepository.findAll();

        for (User u : users) {
            Notification n = Notification.builder()
                    .user(u)
                    .notificationTitle(req.getNotificationTitle())
                    .notificationMessage(req.getNotificationMessage())
                    .type(NotificationType.SYSTEM)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(n);
        }

        return "Đã gửi thông báo toàn hệ thống!";
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {

        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        if (n.getType() == NotificationType.SYSTEM) {
            notificationRepository.deleteAllByNotificationTitleAndNotificationMessageAndType(
                    n.getNotificationTitle(),
                    n.getNotificationMessage(),
                    NotificationType.SYSTEM);
        } else {
            notificationRepository.delete(n);
        }
    }
}
