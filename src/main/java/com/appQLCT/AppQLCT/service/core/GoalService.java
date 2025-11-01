package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.GoalRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Goal;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.GoalRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService; // ✅ Thêm inject NotificationService

    // ✅ Lấy danh sách mục tiêu của user
    public List<Goal> getGoalsByUser() {
        User user = userService.getCurrentUser();
        return goalRepository.findByUser(user);
    }

    // ✅ Tạo mới mục tiêu
    public Goal createGoal(GoalRequest request) {
        User user = userService.getCurrentUser();

        // 🔹 Tìm hoặc tạo danh mục
        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setCategoryName(request.getCategoryName());
                    newCat.setType("goal");
                    return categoryRepository.save(newCat);
                });

        // 🔹 Tìm ví theo tên và user
        Wallet wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + request.getWalletName()));

        // 🔹 Tạo goal mới
        Goal goal = new Goal();
        goal.setUser(user);
        goal.setCategory(category);
        goal.setWallet(wallet);
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(
                request.getCurrentAmount() != null ? request.getCurrentAmount() : BigDecimal.ZERO
        );
        goal.setDeadline(request.getDeadline());
        goal.setStartDate(LocalDate.now());

        goalRepository.save(goal);

        // 🔔 Gửi thông báo sau khi tạo thành công
        notificationService.createNotification(
                user,
                "Tạo mục tiêu mới 🎯",
                "Bạn vừa thêm mục tiêu: " + request.getGoalName(),
                "goal"
        );

        return goal;
    }

    // ✅ Xóa mục tiêu
    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}
