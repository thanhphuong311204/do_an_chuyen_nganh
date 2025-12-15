package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.StreakResponse;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streak")
@RequiredArgsConstructor
public class StreakController {

    private final UserService userService;

    @GetMapping
    public StreakResponse getStreak() {
        User user = userService.getCurrentUser();

        if (user == null) {
            return new StreakResponse(0, 0);
        }

        return new StreakResponse(
                user.getCurrentStreak(),
                user.getLongestStreak()
        );
    }
}
