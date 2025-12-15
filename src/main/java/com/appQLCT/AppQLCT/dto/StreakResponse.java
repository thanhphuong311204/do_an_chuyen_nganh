package com.appQLCT.AppQLCT.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreakResponse {
    private int currentStreak;
    private int maxStreak;
}
