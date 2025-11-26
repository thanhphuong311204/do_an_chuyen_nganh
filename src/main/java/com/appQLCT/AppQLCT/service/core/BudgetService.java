package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.BudgetRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Budget;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.BudgetRepository;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public List<Budget> getBudgetsByUser() {
        User user = userService.getCurrentUser();
        return budgetRepository.findByUser(user);
    }

    public Budget createBudget(BudgetRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setCategoryName(request.getCategoryName());
                    newCat.setType("expense"); 
                    return categoryRepository.save(newCat);
                });

        Wallet wallet = null;
        if (request.getWalletName() != null && !request.getWalletName().isEmpty()) {
            wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + request.getWalletName()));
        }

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .wallet(wallet)
                .amountLimit(request.getAmountLimit())
                .spentAmount(BigDecimal.ZERO)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Budget saved = budgetRepository.save(budget);

        notificationService.createNotification(
                user,
                "Tạo ngân sách mới 💰",
                "Danh mục: " + category.getCategoryName() + 
                " • Giới hạn: " + request.getAmountLimit() + "đ",
                "budget"
        );

        return saved;
    }

    public void checkBudgetLimit(Budget budget) {
        if (budget == null) return;

        BigDecimal spent = budget.getSpentAmount();
        BigDecimal limit = budget.getAmountLimit();

        if (limit.compareTo(BigDecimal.ZERO) == 0) return;

        BigDecimal percent = spent.multiply(BigDecimal.valueOf(100))
                .divide(limit, 2, BigDecimal.ROUND_HALF_UP);

        if (percent.compareTo(BigDecimal.valueOf(100)) >= 0) {
            notificationService.createNotification(
                    budget.getUser(),
                    "Ngân sách vượt giới hạn!",
                    "Bạn đã chi tiêu vượt 100% ngân sách cho " + budget.getCategory().getCategoryName(),
                    "budget"
            );
        } else if (percent.compareTo(BigDecimal.valueOf(80)) >= 0) {
            notificationService.createNotification(
                    budget.getUser(),
                    "⚠️ Gần vượt ngân sách!",
                    "Bạn đã chi tiêu hơn 80% ngân sách cho " + budget.getCategory().getCategoryName(),
                    "budget"
            );
        }
    }
public void updateSpentAmount(Budget budget, BigDecimal totalSpent) {
    budget.setSpentAmount(totalSpent != null ? totalSpent : BigDecimal.ZERO);
    budgetRepository.save(budget);
}


    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
