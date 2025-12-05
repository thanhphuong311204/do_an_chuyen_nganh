package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.repository.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

@GetMapping("/overview")
public Map<String, Object> getOverviewStats() {

    long totalUsers = userRepository.count();
    long totalWallets = walletRepository.count();
    long totalGoals = goalRepository.count();
    long totalRecurring = recurringTransactionRepository.count();

    // Không dùng Optional vì repo bạn trả về BigDecimal trực tiếp
BigDecimal totalIncome = incomeRepository.sumAllIncomes().orElse(BigDecimal.ZERO);
BigDecimal totalExpense = expenseRepository.sumAllExpenses().orElse(BigDecimal.ZERO);


    // Xử lý null
    if (totalIncome == null) totalIncome = BigDecimal.ZERO;
    if (totalExpense == null) totalExpense = BigDecimal.ZERO;

    Map<String, Object> data = new HashMap<>();
    data.put("totalUsers", totalUsers);
    data.put("totalWallets", totalWallets);
    data.put("totalIncome", totalIncome);
    data.put("totalExpense", totalExpense);
    data.put("totalGoals", totalGoals);
    data.put("totalRecurring", totalRecurring);

    return data;
}
}
