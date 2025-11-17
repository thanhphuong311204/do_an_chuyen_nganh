package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.GoalRequest;
import com.appQLCT.AppQLCT.entity.core.Goal;
import com.appQLCT.AppQLCT.service.core.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<List<Goal>> getGoalsByUser() {
        return ResponseEntity.ok(goalService.getGoalsByUser());
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalRequest request) {
        return ResponseEntity.ok(goalService.createGoal(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.ok("Xóa mục tiêu thành công");
    }

    // 🆕 ➕ Cập nhật tiến độ (Flutter gọi API này khi nhấn nút “Thêm tiến độ”)
    @PutMapping("/{id}/progress")
    public ResponseEntity<Goal> updateGoalProgress(
            @PathVariable Long id,
            @RequestBody Map<String, BigDecimal> body) {
        BigDecimal amount = body.get("amount");
        return ResponseEntity.ok(goalService.updateProgress(id, amount));
    }
    
}
