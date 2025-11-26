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

    private String language; 
    private String currency; 
    private Boolean darkMode; 
    private Boolean notificationEnabled; 

    private String theme; 
    private String dateFormat; 
    private Long defaultWalletId;
    private Boolean showBalanceOnHome; 
    private Boolean autoBackup; 
    private String backupFrequency; 
    private LocalTime reminderTime;
    private Boolean budgetAlertEnabled; 
    private Double budgetThreshold; 
}
