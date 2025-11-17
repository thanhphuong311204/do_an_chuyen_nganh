package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.IncomeRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
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
    private final WalletRepository walletRepository;

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

        // ✅ Cập nhật số dư ví
        Wallet wallet = income.getWallet();
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance().add(income.getAmount()));
            walletRepository.save(wallet);
        }

        return ResponseEntity.ok(income);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
