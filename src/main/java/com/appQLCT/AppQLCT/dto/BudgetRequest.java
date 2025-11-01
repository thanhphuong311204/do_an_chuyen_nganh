package com.appQLCT.AppQLCT.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BudgetRequest {
    private BigDecimal amountLimit;
    private String categoryName;
    private String walletName;
    private LocalDate startDate;
    private LocalDate endDate;
}
