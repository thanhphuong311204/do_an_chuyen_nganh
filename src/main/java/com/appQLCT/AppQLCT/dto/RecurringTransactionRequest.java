package com.appQLCT.AppQLCT.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecurringTransactionRequest {
    private String categoryName;
    private BigDecimal amount;
    private String note;
    private String frequency;
    private LocalDate nextDate;
}
