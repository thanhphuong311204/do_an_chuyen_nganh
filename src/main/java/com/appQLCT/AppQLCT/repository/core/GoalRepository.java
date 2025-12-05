package com.appQLCT.AppQLCT.repository.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // Lấy danh sách mục tiêu của 1 user (đã có)
    List<Goal> findByUser(User user);

    // Tổng số mục tiêu
    @Query("SELECT COUNT(g) FROM Goal g")
    Long countTotalGoals();

    // Tổng số mục tiêu đã đạt (current >= target)
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.currentAmount >= g.targetAmount")
    Long countCompletedGoals();

    // Tổng tiền người dùng đã đạt được (lũy kế currentAmount)
    @Query("SELECT COALESCE(SUM(g.currentAmount), 0) FROM Goal g")
    BigDecimal sumTotalCurrent();

    // Tổng tiền mục tiêu cần đạt
    @Query("SELECT COALESCE(SUM(g.targetAmount), 0) FROM Goal g")
    BigDecimal sumTotalTarget();

    // Thống kê theo tháng (để vẽ biểu đồ)
    @Query("""
        SELECT MONTH(g.createdAt) AS month,
               COUNT(g) AS totalGoals,
               SUM(CASE WHEN g.currentAmount >= g.targetAmount THEN 1 ELSE 0 END) AS completedGoals
        FROM Goal g
        GROUP BY MONTH(g.createdAt)
        ORDER BY month ASC
    """)
    List<Map<String, Object>> statsByMonth();
}
