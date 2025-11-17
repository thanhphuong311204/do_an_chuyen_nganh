package com.appQLCT.AppQLCT.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String categoryName;
    private String type;
    private String icon;
}
