package com.appQLCT.AppQLCT.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

@GetMapping("/admin/dashboard")
public String dashboard(Model model) {
    model.addAttribute("page", "admin/dashboard");
    model.addAttribute("title", "Dashboard tổng quan");
    model.addAttribute("active", "dashboard");
    return "admin_layout";
}
}
