package com.appQLCT.AppQLCT.repository.core;

import com.appQLCT.AppQLCT.entity.core.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryName(String categoryName); 
}
