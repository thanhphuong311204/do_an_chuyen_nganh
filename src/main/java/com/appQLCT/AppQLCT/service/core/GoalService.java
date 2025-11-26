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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;

    public List<Goal> getGoalsByUser() {
        User user = userService.getCurrentUser();
        return goalRepository.findByUser(user);
    }

    public Goal createGoal(GoalRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setCategoryName(request.getCategoryName());
                    newCat.setType("goal");
                    return categoryRepository.save(newCat);
                });

        Wallet wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + request.getWalletName()));

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

        Goal saved = goalRepository.save(goal);

        notificationService.createNotification(
                user,
                "Tạo mục tiêu mới 🎯",
                "Bạn vừa thêm mục tiêu: " + request.getGoalName(),
                "goal"
        );

        return saved;
    }

    public Goal updateProgress(Long id, BigDecimal amount) {
        User currentUser = userService.getCurrentUser();


        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy mục tiêu ID: " + id));

        if (goal.getUser() == null) {
            goal.setUser(currentUser);
        }


        if (!Objects.equals(goal.getUser().getId(), currentUser.getId())) {
            goal.setUser(currentUser);
            goal = goalRepository.save(goal);
        }

        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));
        Goal updated = goalRepository.save(goal);

        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            notificationService.createNotification(
                    goal.getUser(),
                    "🎉 Hoàn thành mục tiêu!",
                    "Bạn đã đạt được mục tiêu: " + goal.getGoalName(),
                    "goal"
            );
        } else {
            notificationService.createNotification(
                    goal.getUser(),
                    "Cập nhật tiến độ 🎯",
                    "Bạn vừa thêm " + amount + "đ vào mục tiêu " + goal.getGoalName(),
                    "goal"
            );
        }

        return updated;
    }

    public void deleteGoal(Long id) {
        User currentUser = userService.getCurrentUser();

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy mục tiêu ID: " + id));

        if (!Objects.equals(goal.getUser().getId(), currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa mục tiêu này!");
        }

        goalRepository.delete(goal);

        notificationService.createNotification(
                currentUser,
                "🗑️ Xóa mục tiêu",
                "Bạn đã xóa mục tiêu: " + goal.getGoalName(),
                "goal"
        );

    }
}
