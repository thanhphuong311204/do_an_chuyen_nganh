package com.appQLCT.AppQLCT.repository.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.createAt BETWEEN :start AND :end")
    BigDecimal sumByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
        SELECT SUM(e.amount) FROM Expense e 
        WHERE e.user.id = :userId 
          AND e.category.id = :categoryId 
          AND e.createAt BETWEEN :start AND :end
    """)
    BigDecimal sumByCategoryAndDateRange(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
        SELECT SUM(e.amount) FROM Expense e 
        WHERE e.user.id = :userId 
          AND e.category.id = :categoryId 
          AND e.wallet.id = :walletId 
          AND e.createAt BETWEEN :start AND :end
    """)
    BigDecimal sumByCategoryAndWalletAndDateRange(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("walletId") Long walletId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
