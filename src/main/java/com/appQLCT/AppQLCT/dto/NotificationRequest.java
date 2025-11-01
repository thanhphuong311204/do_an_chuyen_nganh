package com.appQLCT.AppQLCT.dto;

import com.appQLCT.AppQLCT.entity.core.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String notificationTitle;
    private String notificationMessage;
    private Notification type;
}
