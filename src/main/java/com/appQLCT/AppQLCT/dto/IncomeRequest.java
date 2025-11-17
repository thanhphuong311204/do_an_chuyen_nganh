package com.appQLCT.AppQLCT.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeRequest {
    private Double amount;
    private String note;
    private Long categoryId;
    private Long walletId;
    private String incomeDate;
}
