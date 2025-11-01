package com.appQLCT.AppQLCT.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest {
    private String username;
    private String email;
    private String password; // client sẽ gửi plain password, mình hash trước khi lưu
}

