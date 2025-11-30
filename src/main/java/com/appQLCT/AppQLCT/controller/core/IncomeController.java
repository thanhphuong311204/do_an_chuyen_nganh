package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.IncomeRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.service.core.IncomeService;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Income>> getIncomes() {
        User currentUser = userService.getCurrentUser();
        List<Income> incomes = incomeService.getIncomesByUser(currentUser);
        return ResponseEntity.ok(incomes);
    }

    @PostMapping
    public ResponseEntity<Income> createIncome(@RequestBody IncomeRequest request) {
        User currentUser = userService.getCurrentUser();
        Income income = incomeService.createIncome(request, currentUser);
        return ResponseEntity.ok(income);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
