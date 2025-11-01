package com.appQLCT.AppQLCT.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class WalletRequest {
    private Long userId;  
    private String walletName;
    private String type;
    private BigDecimal balance;
}
