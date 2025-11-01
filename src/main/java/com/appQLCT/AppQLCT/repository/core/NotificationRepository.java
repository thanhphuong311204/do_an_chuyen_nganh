package com.appQLCT.AppQLCT.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appQLCT.AppQLCT.entity.core.Notification;

import java.util.List;
import com.appQLCT.AppQLCT.entity.authentic.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
