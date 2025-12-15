package com.appQLCT.AppQLCT.dto;

import lombok.Data;

@Data
public class FcmRequest {
    private String token;
    private String title;
    private String body;
}
