package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.BudgetRequest;
import com.appQLCT.AppQLCT.entity.core.Budget;
import com.appQLCT.AppQLCT.service.core.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets() {
        return ResponseEntity.ok(budgetService.getBudgetsByUser());
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
