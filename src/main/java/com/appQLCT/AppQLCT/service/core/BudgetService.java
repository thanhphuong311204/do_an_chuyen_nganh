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
    private final NotificationService notificationService; // ✅ thêm dòng này

    // ✅ Lấy danh sách ngân sách theo user
    public List<Budget> getBudgetsByUser() {
        User user = userService.getCurrentUser();
        return budgetRepository.findByUser(user);
    }

    // ✅ Tạo mới ngân sách
    public Budget createBudget(BudgetRequest request) {
        User user = userService.getCurrentUser();

        // Tìm danh mục
        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục: " + request.getCategoryName()));

        // Tìm ví (nếu có)
        Wallet wallet = null;
        if (request.getWalletName() != null && !request.getWalletName().isEmpty()) {
            wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + request.getWalletName()));
        }

        // Tạo mới ngân sách
        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .wallet(wallet)
                .amountLimit(request.getAmountLimit())
                .spentAmount(BigDecimal.ZERO)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return budgetRepository.save(budget);
    }

    // ✅ Kiểm tra giới hạn ngân sách (gọi khi update chi tiêu)
    public void checkBudgetLimit(Budget budget) {
        if (budget == null) return;

        BigDecimal spent = budget.getSpentAmount();
        BigDecimal limit = budget.getAmountLimit();

        if (limit.compareTo(BigDecimal.ZERO) == 0) return;

        BigDecimal percent = spent.multiply(BigDecimal.valueOf(100)).divide(limit, 2, BigDecimal.ROUND_HALF_UP);

        if (percent.compareTo(BigDecimal.valueOf(100)) >= 0) {
            notificationService.createNotification(
                    budget.getUser(),
                    "Ngân sách vượt giới hạn!",
                    "Bạn đã chi tiêu vượt 100% ngân sách cho danh mục " + budget.getCategory().getCategoryName(),
                    "budget"
            );
        } else if (percent.compareTo(BigDecimal.valueOf(80)) >= 0) {
            notificationService.createNotification(
                    budget.getUser(),
                    "Cảnh báo sắp vượt ngân sách!",
                    "Bạn đã chi tiêu hơn 80% ngân sách cho danh mục " + budget.getCategory().getCategoryName(),
                    "budget"
            );
        }
    }

    // ✅ Xóa ngân sách
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
