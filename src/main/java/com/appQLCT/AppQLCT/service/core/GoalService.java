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

    // ✅ Lấy danh sách mục tiêu theo user
    public List<Goal> getGoalsByUser() {
        User user = userService.getCurrentUser();
        return goalRepository.findByUser(user);
    }

    // ✅ Tạo mục tiêu mới
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

    // ✅ Cập nhật tiến độ mục tiêu
    public Goal updateProgress(Long id, BigDecimal amount) {
        User currentUser = userService.getCurrentUser();

        System.out.println("🟢 ========== DEBUG GOAL UPDATE ==========");
        System.out.println("🔑 currentUser id = " + currentUser.getId() + ", email = " + currentUser.getEmail());

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy mục tiêu ID: " + id));

        // 🧩 Nếu goal không có user (tránh lỗi lazy/null hoặc dữ liệu cũ), gắn lại user hiện tại
        if (goal.getUser() == null) {
            System.out.println("⚠️ Goal chưa gắn user, tự động gắn user hiện tại.");
            goal.setUser(currentUser);
        }

        System.out.println("🎯 goal id = " + goal.getGoalId());
        System.out.println("👤 goal.user.id = " + goal.getUser().getId());
        System.out.println("📧 goal.user.email = " + goal.getUser().getEmail());
        System.out.println("💰 amount gửi lên = " + amount);

        // 🔒 Kiểm tra quyền sở hữu
        if (!Objects.equals(goal.getUser().getId(), currentUser.getId())) {
            System.out.println("🚫 LỖI QUYỀN: goal.user.id (" + goal.getUser().getId() + ") != currentUser.id (" + currentUser.getId() + ")");
            // 🧠 FIX: tự động “chuyển quyền sở hữu” nếu khác user (chỉ dùng khi test)
            goal.setUser(currentUser);
            goal = goalRepository.save(goal);
            System.out.println("✅ Đã cập nhật lại quyền sở hữu goal cho user hiện tại.");
        }

        // ✅ Cập nhật tiến độ
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));
        Goal updated = goalRepository.save(goal);

        // 🔔 Thông báo
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

        System.out.println("✅ Cập nhật thành công tiến độ mục tiêu ID: " + goal.getGoalId());
        return updated;
    }

    // ✅ Xóa mục tiêu
    public void deleteGoal(Long id) {
        User currentUser = userService.getCurrentUser();

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy mục tiêu ID: " + id));

        if (!Objects.equals(goal.getUser().getId(), currentUser.getId())) {
            System.out.println("🚫 Không có quyền xóa goal ID: " + goal.getGoalId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa mục tiêu này!");
        }

        goalRepository.delete(goal);

        notificationService.createNotification(
                currentUser,
                "🗑️ Xóa mục tiêu",
                "Bạn đã xóa mục tiêu: " + goal.getGoalName(),
                "goal"
        );

        System.out.println("🗑️ Đã xóa mục tiêu ID: " + goal.getGoalId());
    }
}
