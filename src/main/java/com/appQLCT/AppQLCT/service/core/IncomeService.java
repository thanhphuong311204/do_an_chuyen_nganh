package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.IncomeRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.IncomeRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;

    public List<Income> getIncomesByUser(User user) {
        return incomeRepository.findByUser(user);
    }

    public Income createIncome(IncomeRequest request, User user) {

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví ID: " + request.getWalletId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục ID: " + request.getCategoryId()));

        Income income = new Income();
        income.setUser(user);
        income.setCategory(category);
        income.setWallet(wallet);
        income.setAmount(BigDecimal.valueOf(request.getAmount()));
        income.setNote(request.getNote());
        income.setIncomeDate(LocalDate.now());

        Income saved = incomeRepository.save(income);

        if (wallet.getBalance() == null) wallet.setBalance(BigDecimal.ZERO);
        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(request.getAmount())));
        walletRepository.save(wallet);

        notificationService.createNotification(
                user,
                "Nhận thu nhập mới 💵",
                "Bạn vừa nhận " + request.getAmount() + " vào danh mục " + category.getCategoryName() +
                        " từ ví " + wallet.getWalletName(),
                "transaction"
        );

        return saved;
    }

    public void deleteIncome(Long id) {
        Income deleted = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thu nhập!"));

        Wallet wallet = deleted.getWallet();
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance().subtract(deleted.getAmount()));
            walletRepository.save(wallet);
        }

        incomeRepository.deleteById(id);

        notificationService.createNotification(
                deleted.getUser(),
                "Xóa thu nhập ❌",
                "Bạn vừa xóa khoản thu nhập trong danh mục " + deleted.getCategory().getCategoryName(),
                "transaction"
        );
    }
}
