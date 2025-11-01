package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.repository.core.RecurringTransactionRepository;
import com.appQLCT.AppQLCT.dto.RecurringTransactionRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.RecurringTransaction;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final NotificationService notificationService; // ✅ thêm vào

    // ✅ Lấy danh sách giao dịch định kỳ của user
    public List<RecurringTransaction> getAllByUser() {
        User user = userService.getCurrentUser();
        return recurringTransactionRepository.findByUser(user);
    }

    // ✅ Tạo giao dịch định kỳ mới
    public RecurringTransaction createRecurring(RecurringTransactionRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục: " + request.getCategoryName()));

        RecurringTransaction recurring = RecurringTransaction.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .note(request.getNote())
                .frequency(request.getFrequency())
                .nextDate(request.getNextDate())
                .build();

        RecurringTransaction saved = recurringTransactionRepository.save(recurring);

        // 🔔 Gửi thông báo khi tạo giao dịch định kỳ mới
        notificationService.createNotification(
                user,
                "Tạo giao dịch định kỳ 🔁",
                "Bạn đã tạo giao dịch định kỳ \"" + recurring.getNote() +
                        "\" với chu kỳ " + recurring.getFrequency().toLowerCase() +
                        " — số tiền: " + recurring.getAmount(),
                "transaction"
        );

        return saved;
    }

    // ✅ Xóa giao dịch định kỳ
    public void deleteRecurring(Long id) {
        RecurringTransaction recurring = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch định kỳ!"));

        recurringTransactionRepository.deleteById(id);

        // 🔔 Gửi thông báo khi xóa giao dịch định kỳ
        notificationService.createNotification(
                recurring.getUser(),
                "Xóa giao dịch định kỳ ❌",
                "Bạn vừa xóa giao dịch định kỳ \"" + recurring.getNote() + "\".",
                "transaction"
        );
    }
}
