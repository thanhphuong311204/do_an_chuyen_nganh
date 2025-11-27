package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import com.appQLCT.AppQLCT.enu.AuthProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "users", layout = MainLayout.class)
public class UserView extends VerticalLayout {

    private final UserRepository userRepository;
    private Grid<User> grid = new Grid<>(User.class);
    private Binder<User> binder = new Binder<>(User.class);

    @Autowired
    public UserView(UserRepository userRepository) {
        this.userRepository = userRepository;

        add(new H2("Quản lý Người dùng"));

        configureGrid();
        VerticalLayout gridContainer = new VerticalLayout(grid);
        gridContainer.addClassName("grid-container");
        add(gridContainer);

        Button addButton = new Button("Thêm mới", e -> openUserDialog(new User()));
        addButton.addClassName("add-button");
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "username", "email", "fullName", "phone", "role", "active");
        grid.addComponentColumn(user -> {
            Button editButton = new Button("Sửa", e -> openUserDialog(user));
            Button deleteButton = new Button("Xóa", e -> deleteUser(user));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openUserDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.addClassName("form-dialog");
        FormLayout form = new FormLayout();

        TextField usernameField = new TextField("Tên đăng nhập");
        TextField emailField = new TextField("Email");
        TextField fullNameField = new TextField("Họ tên");
        TextField phoneField = new TextField("Số điện thoại");
        TextField roleField = new TextField("Vai trò");
        Checkbox activeField = new Checkbox("Hoạt động");
        Select<AuthProvider> authProviderField = new Select<>();
        authProviderField.setLabel("Nhà cung cấp xác thực");
        authProviderField.setItems(AuthProvider.values());

        binder.bind(usernameField, User::getUsername, User::setUsername);
        binder.bind(emailField, User::getEmail, User::setEmail);
        binder.bind(fullNameField, User::getFullName, User::setFullName);
        binder.bind(phoneField, User::getPhone, User::setPhone);
        binder.bind(roleField, User::getRole, User::setRole);
        binder.bind(activeField, User::isActive, User::setActive);
        binder.bind(authProviderField, User::getAuthProvider, User::setAuthProvider);

        binder.setBean(user);

        form.add(usernameField, emailField, fullNameField, phoneField, roleField, activeField, authProviderField);

        Button saveButton = new Button("Lưu", e -> saveUser(user, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveUser(User user, Dialog dialog) {
        try {
            userRepository.save(user);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        userRepository.delete(user);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(userRepository.findAll());
    }
}