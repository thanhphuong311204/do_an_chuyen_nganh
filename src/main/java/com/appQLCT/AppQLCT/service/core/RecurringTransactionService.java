package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.ExpenseRequest;
import com.appQLCT.AppQLCT.dto.RecurringTransactionRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.RecurringTransaction;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.RecurringTransactionRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringRepo;
    private final CategoryRepository categoryRepo;
    private final WalletRepository walletRepo;
    private final UserService userService;
    private final ExpenseService expenseService;
    private final NotificationService notiService;

    public List<RecurringTransaction> getAllByUser() {
        User user = userService.getCurrentUser();
        return recurringRepo.findByUser(user);
    }

    public RecurringTransaction createRecurring(RecurringTransactionRequest request) {

        User user = userService.getCurrentUser();

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy danh mục ID: " + request.getCategoryId()));
        }

        if (category == null && request.getCategoryId() != null) {
            category = categoryRepo.findByCategoryId(request.getCategoryId())
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy danh mục: " + request.getCategoryId()));
        }

        if (category == null) {
            throw new RuntimeException("Thiếu categoryId hoặc categoryName!");
        }

        Wallet wallet = walletRepo.findByUser(user)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User chưa có ví!"));

        RecurringTransaction recurring = RecurringTransaction.builder()
                .user(user)
                .category(category)
                .categoryName(category.getCategoryName())
                .wallet(wallet)
                .amount(request.getAmount())
                .note(request.getNote())
                .frequency(request.getFrequency())
                .nextDate(request.getNextDate())
                .build();

        RecurringTransaction saved = recurringRepo.save(recurring);

        notiService.createNotification(
                user,
                "Tạo giao dịch định kỳ 🔁",
                "Bạn vừa tạo giao dịch định kỳ \"" + request.getNote()
                        + "\" với chu kỳ " + request.getFrequency(),
                "transaction"
        );

        return saved;
    }

    public void deleteRecurring(Long id) {
        RecurringTransaction r = recurringRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy recurring transaction!"));

        recurringRepo.deleteById(id);

        notiService.createNotification(
                r.getUser(),
                "Xóa giao dịch định kỳ ❌",
                "Bạn đã xóa giao dịch \"" + r.getNote() + "\"",
                "transaction"
        );
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void autoGenerateRecurringTransactions() {

        List<RecurringTransaction> list = recurringRepo.findAll();
        LocalDate today = LocalDate.now();

        for (RecurringTransaction r : list) {

            if (!r.getNextDate().isEqual(today)) continue;

            ExpenseRequest req = new ExpenseRequest();
            req.setWalletId(r.getWallet().getId());
            req.setCategoryId(r.getCategory().getCategoryId());
            req.setAmount(r.getAmount().doubleValue());
            req.setNote(r.getNote());
            req.setCreatedAt(today.toString());

            expenseService.createExpense(req, r.getUser());

            r.setNextDate(calculateNextDate(r.getNextDate(), r.getFrequency()));
            recurringRepo.save(r);

            notiService.createNotification(
                    r.getUser(),
                    "Giao dịch định kỳ đã chạy 🔁",
                    "Đã tạo giao dịch \"" + r.getNote() +
                            "\" số tiền " + r.getAmount(),
                    "transaction"
            );
        }
    }

    private LocalDate calculateNextDate(LocalDate date, String frequency) {
        return switch (frequency.toLowerCase()) {
            case "daily" -> date.plusDays(1);
            case "weekly" -> date.plusWeeks(1);
            case "monthly" -> date.plusMonths(1);
            case "yearly" -> date.plusYears(1);
            default -> date;
        };
    }
}
