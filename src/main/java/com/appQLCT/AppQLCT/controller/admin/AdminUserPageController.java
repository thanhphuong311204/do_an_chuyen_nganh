package com.appQLCT.AppQLCT.controller.admin;

import com.appQLCT.AppQLCT.repository.core.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminUserPageController {

    private final UserRepository userRepository;

@GetMapping("/admin/users")
public String usersPage(Model model) {
    model.addAttribute("page", "admin/users"); 
    model.addAttribute("title", "Quản lý người dùng");
    model.addAttribute("active", "users");
    return "admin_layout";
}

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("page", "admin_users");
        model.addAttribute("title", "Quản lý người dùng");
        model.addAttribute("active", "users");
        return "admin_layout";
    }
}
