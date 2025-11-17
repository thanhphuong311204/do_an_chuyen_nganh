package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.WalletRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final NotificationService notificationService; // ✅ thông báo

    // ✅ Lấy tất cả ví của user
    public List<Wallet> getWalletsByUserId(Long userId) {
        return walletRepository.findByUser_Id(userId);
    }

    // ✅ Tạo ví mới
    public Wallet createWallet(WalletRequest request, User user) {
        Wallet wallet = Wallet.builder()
                .walletName(request.getWalletName())
                .balance(request.getBalance())
                .type(request.getType())
                .user(user)
                .build();

        Wallet saved = walletRepository.save(wallet);

        // 🔔 Thông báo khi tạo ví mới
        notificationService.createNotification(
                user,
                "Tạo ví mới 💼",
                "Bạn vừa thêm ví \"" + saved.getWalletName() + "\" với số dư ban đầu " + saved.getBalance() + ".",
                "system"
        );

        return saved;
    }

    // ✅ Cập nhật ví
    public Wallet updateWallet(Long walletId, WalletRequest request, User user) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví!"));

        // ⚙️ In log để debug quyền user
        System.out.println("🧾 Ví thuộc user_id: " + wallet.getUser().getId());
        System.out.println("👤 Người đăng nhập user_id: " + user.getId());

        if (!wallet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("❌ Không có quyền sửa ví này!");
        }

        wallet.setWalletName(request.getWalletName());
        wallet.setBalance(request.getBalance());
        wallet.setType(request.getType());

        Wallet updated = walletRepository.save(wallet);

        // 🔔 Thông báo khi cập nhật ví
        notificationService.createNotification(
                user,
                "Cập nhật ví 🔧",
                "Ví \"" + updated.getWalletName() + "\" đã được cập nhật — số dư hiện tại: " + updated.getBalance(),
                "system"
        );

        return updated;
    }

    // ✅ Xóa ví
    public void deleteWallet(Long walletId, User user) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví!"));

        // ⚙️ In log ra để xem lỗi 403 có phải do user không khớp không
        System.out.println("🧾 Ví thuộc user_id: " + wallet.getUser().getId());
        System.out.println("👤 Người đăng nhập user_id: " + user.getId());

        if (!wallet.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("❌ Không có quyền xóa ví này!");
        }

        try {
            // ✅ Xóa ví
            walletRepository.delete(wallet);

            // 🔔 Thông báo khi xóa ví
            notificationService.createNotification(
                    user,
                    "Xóa ví ❌",
                    "Bạn vừa xóa ví \"" + wallet.getWalletName() + "\" khỏi hệ thống.",
                    "system"
            );

            System.out.println("✅ Đã xóa ví thành công: " + wallet.getWalletName());

        } catch (DataIntegrityViolationException e) {
            // ⚠️ Nếu ví đang có giao dịch (bị ràng buộc FK)
            System.out.println("⚠️ Không thể xóa ví vì đang có giao dịch liên kết!");
            throw new RuntimeException("⚠️ Ví đang có giao dịch, không thể xóa!");
        }
    }
}
