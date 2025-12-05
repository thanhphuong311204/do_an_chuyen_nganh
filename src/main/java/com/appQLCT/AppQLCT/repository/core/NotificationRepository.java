package com.appQLCT.AppQLCT.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appQLCT.AppQLCT.entity.core.Notification;
import com.appQLCT.AppQLCT.entity.core.NotificationType;
import com.appQLCT.AppQLCT.entity.authentic.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByTypeOrderByCreatedAtDesc(NotificationType type);

    void deleteAllByNotificationTitleAndNotificationMessageAndType(
            String notificationTitle,
            String notificationMessage,
            NotificationType type);
}
