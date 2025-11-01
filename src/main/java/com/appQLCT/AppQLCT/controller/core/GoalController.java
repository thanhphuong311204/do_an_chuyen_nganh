package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.GoalRequest;
import com.appQLCT.AppQLCT.entity.core.Goal;
import com.appQLCT.AppQLCT.service.core.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
