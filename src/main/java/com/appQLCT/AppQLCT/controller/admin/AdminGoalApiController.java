package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.repository.core.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/goals")
public class AdminGoalApiController {

    // 🔸 Spring sẽ tự inject GoalRepository (liên kết tới DB)
    private final GoalRepository goalRepository;

    // 🔹 API trả về danh sách mục tiêu (JSON)
    @GetMapping("/list")
    public List<Map<String, Object>> getAllGoalsForAdmin() {
        return goalRepository.findAll().stream().map(goal -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", goal.getGoalId());
            map.put("name", goal.getGoalName());
            map.put("target", goal.getTargetAmount());
            map.put("current", goal.getCurrentAmount());

            double progress = 0;
            if (goal.getTargetAmount() != null && goal.getTargetAmount().doubleValue() > 0) {
                progress = (goal.getCurrentAmount().doubleValue() / goal.getTargetAmount().doubleValue()) * 100.0;
            }
            map.put("progress", Math.round(progress * 10.0) / 10.0);
            map.put("category", goal.getCategory() != null ? goal.getCategory().getCategoryName() : "Không rõ");

            return map;
        }).toList();
    }
}
