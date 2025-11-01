package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.IncomeRequest;
import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.service.core.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping
    public List<Income> getIncomes() {
        return incomeService.getIncomesByUser();
    }

    @PostMapping
    public Income createIncome(@RequestBody IncomeRequest request) {
        return incomeService.createIncome(request);
    }

    @DeleteMapping("/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}
