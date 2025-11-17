package com.appQLCT.AppQLCT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép tất cả origin (phục vụ Flutter web, localhost)
        config.addAllowedOriginPattern("*");

        // ✅ Cho phép tất cả header và method
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // ✅ Cho phép gửi cookie/token nếu cần
        config.setAllowCredentials(true);

        // ✅ Áp dụng cho tất cả endpoint
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
