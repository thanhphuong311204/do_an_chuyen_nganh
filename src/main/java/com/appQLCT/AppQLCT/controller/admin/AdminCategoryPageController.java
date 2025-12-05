package com.appQLCT.AppQLCT.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCategoryPageController {

    @GetMapping("/admin/categories")
    public String showCategories(Model model) {
        model.addAttribute("title", "Quản lý danh mục");
        model.addAttribute("page", "admin/categories"); 
        model.addAttribute("active", "categories");
        return "admin_layout";
    }
    
}
