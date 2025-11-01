package com.appQLCT.AppQLCT.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeRequest {
    private BigDecimal amount;
    private String note;
    private LocalDate incomeDate;
    private String categoryName;
    private String walletName;

}
