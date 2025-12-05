package com.appQLCT.AppQLCT.repository.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId AND i.incomeDate BETWEEN :start AND :end")
BigDecimal sumByUserAndDateRange(@Param("userId") Long userId,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);

    @Query("SELECT SUM(i.amount) FROM Income i")
Optional<BigDecimal> sumAllIncomes();

}
