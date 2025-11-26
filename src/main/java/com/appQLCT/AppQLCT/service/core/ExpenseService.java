package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.ExpenseRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.*;
import com.appQLCT.AppQLCT.repository.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;
    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;

    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense createExpense(ExpenseRequest request, User user) {

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví ID: " + request.getWalletId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục ID: " + request.getCategoryId()));

        Expense expense = Expense.builder()
                .amount(BigDecimal.valueOf(request.getAmount()))
                .note(request.getNote())
                .category(category)
                .wallet(wallet)
                .user(user)
                .createAt(LocalDate.now())
                .build();

        Expense saved = expenseRepository.save(expense);

        if (wallet.getBalance() == null) wallet.setBalance(BigDecimal.ZERO);
        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(request.getAmount())));
        walletRepository.save(wallet);

        notificationService.createNotification(
                user,
                "Thêm chi tiêu mới 💸",
                "Bạn vừa thêm " + request.getAmount() + " vào danh mục " +
                        category.getCategoryName() + " từ ví " + wallet.getWalletName(),
                "transaction"
        );

        updateRelatedBudgets(user, category, wallet);

        return saved;
    }

    public Expense updateExpense(Long id, ExpenseRequest request, User user) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu!"));

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví ID: " + request.getWalletId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục ID: " + request.getCategoryId()));

        existing.setAmount(BigDecimal.valueOf(request.getAmount()));
        existing.setNote(request.getNote());
        existing.setWallet(wallet);
        existing.setCategory(category);
        existing.setCreateAt(LocalDate.now());

        Expense updated = expenseRepository.save(existing);

        notificationService.createNotification(
                user,
                "Cập nhật chi tiêu 🧾",
                "Bạn vừa chỉnh sửa chi tiêu trong danh mục " + category.getCategoryName(),
                "transaction"
        );

        updateRelatedBudgets(user, category, wallet);
        return updated;
    }

public void deleteExpense(Long id) {
    Expense deleted = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu!"));

    Wallet wallet = deleted.getWallet();
    Category category = deleted.getCategory();
    User user = deleted.getUser();

    if (wallet != null) {
        wallet.setBalance(wallet.getBalance().add(deleted.getAmount()));
        walletRepository.save(wallet);
    }

    expenseRepository.deleteById(id);

    updateRelatedBudgets(user, category, wallet);

    notificationService.createNotification(
            user,
            "Xóa chi tiêu ❌",
            "Bạn vừa xóa chi tiêu trong danh mục " + category.getCategoryName(),
            "transaction"
    );
}
    private void updateRelatedBudgets(User user, Category category, Wallet wallet) {
        List<Budget> budgets = budgetRepository.findByUser(user);

        for (Budget b : budgets) {
            if (b.getCategory().getCategoryId().equals(category.getCategoryId())) {
                BigDecimal totalSpent;

                if (b.getWallet() != null) {
                    totalSpent = expenseRepository.sumByCategoryAndWalletAndDateRange(
                            user.getId(),
                            b.getCategory().getCategoryId(),
                            b.getWallet().getId(),
                            b.getStartDate(),
                            b.getEndDate()
                    );
                } else {
                    totalSpent = expenseRepository.sumByCategoryAndDateRange(
                            user.getId(),
                            b.getCategory().getCategoryId(),
                            b.getStartDate(),
                            b.getEndDate()
                    );
                }

                budgetService.updateSpentAmount(b, totalSpent);
                budgetService.checkBudgetLimit(b);
            }
        }
    }
}
