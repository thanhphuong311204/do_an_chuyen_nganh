package com.appQLCT.AppQLCT.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // ✅ Cấu hình cho Thymeleaf hoặc HTML
        registry.jsp("/templates/", ".html");
    }
}
