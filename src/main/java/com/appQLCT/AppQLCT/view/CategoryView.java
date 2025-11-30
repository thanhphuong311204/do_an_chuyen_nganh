package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "categories", layout = MainLayout.class)
public class CategoryView extends VerticalLayout {

    private final CategoryRepository categoryRepository;
    private final Grid<Category> grid = new Grid<>(Category.class);
    private final Binder<Category> binder = new Binder<>(Category.class);

    @Autowired
    public CategoryView(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

        add(new H2("Quản lý Danh mục"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openCategoryDialog(new Category()));
        add(addButton);

        updateList();
    }

    /**
     * Cấu hình bảng hiển thị danh sách danh mục
     */
    private void configureGrid() {
        // Quan trọng: với boolean getter là isPublic() thì property name là "public"
        grid.setColumns("categoryId", "categoryName", "type", "public", "iconUrl");

        grid.addComponentColumn(category -> {
            Button editButton = new Button("Sửa", e -> openCategoryDialog(category));
            Button deleteButton = new Button("Xóa", e -> deleteCategory(category));
            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader("Thao tác");
    }

    /**
     * Mở dialog để thêm/sửa danh mục
     */
    private void openCategoryDialog(Category category) {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        TextField categoryNameField = new TextField("Tên danh mục");
        TextField typeField = new TextField("Loại");
        Checkbox isPublicField = new Checkbox("Công khai");
        TextField iconUrlField = new TextField("URL biểu tượng");

        // Dùng forField() thay vì bind() để Vaadin hiểu rõ kiểu dữ liệu của component
        binder.forField(categoryNameField)
                .bind(Category::getCategoryName, Category::setCategoryName);

        binder.forField(typeField)
                .bind(Category::getType, Category::setType);

        binder.forField(isPublicField)
                .bind(Category::isPublic, Category::setIsPublic);

        binder.forField(iconUrlField)
                .bind(Category::getIconUrl, Category::setIconUrl);

        binder.setBean(category);

        form.add(categoryNameField, typeField, isPublicField, iconUrlField);

        Button saveButton = new Button("Lưu", e -> saveCategory(category, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    /**
     * Lưu danh mục vào database
     */
    private void saveCategory(Category category, Dialog dialog) {
        try {
            categoryRepository.save(category);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Lỗi khi lưu: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    /**
     * Xóa danh mục
     */
    private void deleteCategory(Category category) {
        try {
            categoryRepository.delete(category);
            updateList();
            Notification.show("Xóa thành công", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Lỗi khi xóa: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void updateList() {
        grid.setItems(categoryRepository.findAll());
    }
}
