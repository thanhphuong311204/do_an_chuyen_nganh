package com.appQLCT.AppQLCT.dto;

import lombok.Data;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReportRequest {
    private String reportType;   
    private LocalDate startDate;
    private LocalDate endDate;
}
