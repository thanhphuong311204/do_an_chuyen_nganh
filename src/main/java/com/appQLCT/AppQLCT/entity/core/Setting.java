package com.appQLCT.AppQLCT.entity.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // --- Cài đặt cơ bản ---
    private String language; // vi, en,...
    private String currency; // VND, USD,...
    private Boolean darkMode; // chế độ tối
    private Boolean notificationEnabled; // bật/tắt thông báo

    // --- Cài đặt mở rộng ---
    private String theme; // light, dark, custom
    private String dateFormat; // dd/MM/yyyy, MM-dd-yyyy,...
    private Long defaultWalletId; // ví mặc định khi thêm giao dịch
    private Boolean showBalanceOnHome; // hiển thị số dư
    private Boolean autoBackup; // bật sao lưu tự động
    private String backupFrequency; // daily, weekly, monthly
    private LocalTime reminderTime; // giờ nhắc nhở ghi chi tiêu
    private Boolean budgetAlertEnabled; // bật/tắt cảnh báo ngân sách
    private Double budgetThreshold; // % cảnh báo ngân sách
}
