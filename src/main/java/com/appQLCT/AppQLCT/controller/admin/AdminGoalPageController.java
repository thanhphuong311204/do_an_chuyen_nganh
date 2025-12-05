package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.repository.core.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminGoalPageController {

    private final GoalRepository goalRepository;

    @GetMapping("/admin/goals")
    public String showGoalsPage(Model model) {
        model.addAttribute("title", "Quản lý mục tiêu hệ thống");
        model.addAttribute("page", "admin/goals");
        model.addAttribute("active", "goals");

        // 🧩 Thống kê thực tế
        Long totalGoals = goalRepository.countTotalGoals();
        Long completedGoals = goalRepository.countCompletedGoals();
        BigDecimal totalTarget = goalRepository.sumTotalTarget();
        BigDecimal totalCurrent = goalRepository.sumTotalCurrent();

        // Tính tỷ lệ hoàn thành (phòng chia cho 0)
        double completionRate = (totalGoals != 0)
                ? (completedGoals * 100.0 / totalGoals)
                : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalGoals", totalGoals);
        stats.put("completedGoals", completedGoals);
        stats.put("completionRate", Math.round(completionRate * 10.0) / 10.0);
        stats.put("totalSaved", totalCurrent);
        stats.put("totalTarget", totalTarget);

        model.addAttribute("stats", stats);

        return "admin_layout";
    }
}
