package com.appQLCT.AppQLCT.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionRequest {
    private Long categoryId;
    private BigDecimal amount;
    private String note;
    private String frequency;
    private LocalDate nextDate;
}
