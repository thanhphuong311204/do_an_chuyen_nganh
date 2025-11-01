package com.appQLCT.AppQLCT.entity.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @Column(name = "amount" ,nullable = false)
    private BigDecimal amount;

    @Column( name = "note",length = 255)
    private String note;

    @Column(name = "created_at")
    private LocalDate createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private Wallet wallet;


    @PrePersist
    void onCreate () {
        if (createAt == null) {
            this.createAt = LocalDate.now();
        }
    }
}
