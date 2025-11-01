package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ✅ Lấy toàn bộ category
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ Lấy category theo ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }
}
