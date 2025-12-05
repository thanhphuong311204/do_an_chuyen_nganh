package com.appQLCT.AppQLCT.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminNotificationPageController {

    @GetMapping("/admin/notifications")
    public String showNotificationsPage(Model model) {
        model.addAttribute("title", "Quản lý thông báo hệ thống");
        model.addAttribute("page", "admin/admin_notifications");
        model.addAttribute("active", "notifications");
        return "admin_layout";
    }
}
