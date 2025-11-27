package com.appQLCT.AppQLCT.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String createdAt;
    private int walletCount;
    private double totalBalance;
}
