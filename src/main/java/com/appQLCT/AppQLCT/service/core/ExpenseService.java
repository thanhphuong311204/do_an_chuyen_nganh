package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.ExpenseRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Expense;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.ExpenseRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService; // ✅ thêm vào

    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense createExpense(ExpenseRequest request, User user) {

        Wallet wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví!"));

        List<Category> categories = categoryRepository.findByCategoryName(request.getCategoryName());
        if (categories.isEmpty()) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
        Category category = categories.get(0); // lấy danh mục đầu tiên nếu trùng tên

        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .note(request.getNote())
                .category(category)
                .wallet(wallet)
                .user(user)
                .build();

        Expense saved = expenseRepository.save(expense);

        // 🔔 Gửi thông báo sau khi tạo chi tiêu
        notificationService.createNotification(
                user,
                "Thêm chi tiêu mới 💸",
                "Bạn vừa thêm chi tiêu " + request.getAmount() + " vào danh mục " + category.getCategoryName() +
                        " từ ví " + wallet.getWalletName(),
                "transaction"
        );

        return saved;
    }

    public Expense updateExpense(Long id, ExpenseRequest request, User user) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu!"));

        Wallet wallet = walletRepository.findByWalletNameAndUser(request.getWalletName(), user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví!"));

        List<Category> categories = categoryRepository.findByCategoryName(request.getCategoryName());
        if (categories.isEmpty()) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
        Category category = categories.get(0);

        existing.setAmount(request.getAmount());
        existing.setNote(request.getNote());
        existing.setWallet(wallet);
        existing.setCategory(category);

        Expense updated = expenseRepository.save(existing);

        // 🔔 Thông báo khi cập nhật chi tiêu
        notificationService.createNotification(
                user,
                "Cập nhật chi tiêu 🧾",
                "Bạn vừa chỉnh sửa chi tiêu trong danh mục " + category.getCategoryName(),
                "transaction"
        );

        return updated;
    }

    public void deleteExpense(Long id) {
        Expense deleted = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu!"));

        expenseRepository.deleteById(id);

        // 🔔 Thông báo khi xóa chi tiêu
        notificationService.createNotification(
                deleted.getUser(),
                "Xóa chi tiêu ❌",
                "Bạn vừa xóa chi tiêu thuộc danh mục " + deleted.getCategory().getCategoryName(),
                "transaction"
        );
    }
}
