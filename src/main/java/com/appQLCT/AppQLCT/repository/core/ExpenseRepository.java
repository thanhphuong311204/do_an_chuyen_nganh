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

    // Lấy tất cả expense của user
    List<Expense> findByUser(User user);

    // Tính tổng chi tiêu trong khoảng thời gian
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.createAt BETWEEN :start AND :end")

    BigDecimal sumByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
