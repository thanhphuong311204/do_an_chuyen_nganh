package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Notification;
import com.appQLCT.AppQLCT.entity.core.NotificationType;
import com.appQLCT.AppQLCT.repository.core.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 🟢 Lấy tất cả thông báo theo user
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // 🟡 Đánh dấu 1 thông báo đã đọc
    public void markAsRead(Long id) {
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        noti.setIsRead(true);
        notificationRepository.save(noti);
    }

    // ⭐ ĐÁNH DẤU TẤT CẢ ĐÃ ĐỌC ⭐
    public void markAllAsRead(User user) {
        List<Notification> list = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        list.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(list);
    }

    // 🟢 Tạo thông báo mới
    public Notification createNotification(User user, String title, String message, String type) {
        NotificationType notiType;
        try {
            notiType = NotificationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            notiType = NotificationType.SYSTEM;
        }

        Notification notification = Notification.builder()
                .user(user)
                .notificationTitle(title)
                .notificationMessage(message)
                .type(notiType)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    // 🔴 Xóa thông báo
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
