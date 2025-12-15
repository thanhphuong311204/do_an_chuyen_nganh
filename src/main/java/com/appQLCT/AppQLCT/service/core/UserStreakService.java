package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserStreakService {

    private final UserRepository userRepository;

    public User updateStreak(User user, LocalDate today) {

        LocalDate lastDate = user.getLastActivityDate();

        // Chưa từng ghi chép
        if (lastDate == null) {
            user.setCurrentStreak(1);
            user.setLongestStreak(1);
            user.setLastActivityDate(today);
            return userRepository.save(user);
        }

        // Ghi lại trong cùng ngày
        if (lastDate.isEqual(today)) {
            return user;
        }

        // Hôm nay = hôm qua → streak +1
        if (lastDate.plusDays(1).isEqual(today)) {
            int newStreak = user.getCurrentStreak() + 1;
            user.setCurrentStreak(newStreak);

            if (newStreak > user.getLongestStreak()) {
                user.setLongestStreak(newStreak);
            }

        } else {
            // Không liên tục → reset streak
            user.setCurrentStreak(1);
        }

        user.setLastActivityDate(today);
        return userRepository.save(user);
    }
}
