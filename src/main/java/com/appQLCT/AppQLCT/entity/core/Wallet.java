package com.appQLCT.AppQLCT.entity.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor                
@AllArgsConstructor               
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Column(name = "type", nullable = false)
    private String type;

    @Builder.Default    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"wallets", "expenses", "hibernateLazyInitializer", "handler"})
    private User user;

    
}
