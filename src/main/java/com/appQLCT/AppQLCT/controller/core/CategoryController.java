package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.service.core.CategoryService;
import com.appQLCT.AppQLCT.service.core.CloudinaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CategoryRepository categoryRepository;

    
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadCategoryIcon(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // 1️⃣ Upload lên Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file);

            // 2️⃣ Tìm category theo ID
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy category có ID: " + id));

            // 3️⃣ Cập nhật link ảnh vào cột icon
            category.setIconUrl(imageUrl);
            categoryRepository.save(category);

            // 4️⃣ Trả về JSON kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upload thành công!");
            response.put("categoryId", category.getCategoryId());
            response.put("iconUrl", imageUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload thất bại: " + e.getMessage()));
        }
    }
}
