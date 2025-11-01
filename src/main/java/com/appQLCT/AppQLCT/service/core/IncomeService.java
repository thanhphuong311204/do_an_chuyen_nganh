package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.IncomeRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public List<Income> getIncomesByUser() {
        User user = userService.getCurrentUser();
        return incomeRepository.findByUser(user);
    }

    public Income createIncome(IncomeRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục: " + request.getCategoryName()));

        Income income = new Income();
        income.setUser(user);
        income.setCategory(category);
        income.setAmount(request.getAmount());
        income.setNote(request.getNote());
        income.setIncomeDate(request.getIncomeDate());
        incomeRepository.save(income);
        // 🔔 Gửi thông báo thu nhập
        notificationService.createNotification(
                user,
                "Nhận thu nhập mới 💵",
                "Bạn vừa nhận: " + request.getAmount() + " vào ví " + request.getWalletName(),
                "transaction"
        );

        return income;
    }
    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }
}
