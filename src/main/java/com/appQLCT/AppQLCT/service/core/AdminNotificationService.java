package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminNotificationService {

    private final UserRepository userRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    // Gửi cho toàn bộ người dùng
    public void sendNotificationToAll(String title, String message) {
        var users = userRepository.findAll();

        for (User user : users) {
            if (user.getFcmToken() != null) {
                firebaseMessagingService.sendToToken(user.getFcmToken(), title, message);
            }
        }
    }

    // Gửi cho một người dùng
    public void sendNotificationToUser(Long userId, String title, String message) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getFcmToken() == null) return;

        firebaseMessagingService.sendToToken(user.getFcmToken(), title, message);
    }
}
