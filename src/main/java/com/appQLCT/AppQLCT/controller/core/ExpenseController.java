package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.ExpenseRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Expense;
import com.appQLCT.AppQLCT.service.core.ExpenseService;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Expense>> getUserExpenses() {
        User currentUser = userService.getCurrentUser(); // ✅ không cần Principal nữa
        List<Expense> expenses = expenseService.getExpensesByUser(currentUser);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseRequest request) {
        User currentUser = userService.getCurrentUser(); // ✅
        Expense expense = expenseService.createExpense(request, currentUser);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request
    ) {
        User currentUser = userService.getCurrentUser(); // ✅
        Expense updated = expenseService.updateExpense(id, request, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
