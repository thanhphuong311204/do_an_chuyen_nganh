package com.appQLCT.AppQLCT.controller.auth;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.service.core.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;
    @GetMapping("/login")
    public String showLoginPage() {
        System.out.println("Accessing /admin/login");
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
    
        User admin = adminService.authenticateAdmin(email, password);
        

        if (admin != null) {
            model.addAttribute("ADMIN", admin);
            System.out.println("Tôi đã vào admin");
            return "admin_dashboard"; 
        } else {
            model.addAttribute("error", "Tài khoản không tồn tại, mật khẩu sai, hoặc bạn không có quyền truy cập!");
            return "login";
        }
        
    }
}
