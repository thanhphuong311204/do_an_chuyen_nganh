package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Goal;
import com.appQLCT.AppQLCT.repository.core.GoalRepository;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import com.appQLCT.AppQLCT.repository.core.CategoryRepository;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Category;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "goals", layout = MainLayout.class)
public class GoalView extends VerticalLayout {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private Grid<Goal> grid = new Grid<>(Goal.class);
    private Binder<Goal> binder = new Binder<>(Goal.class);

    @Autowired
    public GoalView(GoalRepository goalRepository, UserRepository userRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

        add(new H2("Quản lý Mục tiêu"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openGoalDialog(new Goal()));
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("goalId", "goalName", "targetAmount", "currentAmount", "deadline", "startDate");
        grid.addComponentColumn(goal -> {
            Button editButton = new Button("Sửa", e -> openGoalDialog(goal));
            Button deleteButton = new Button("Xóa", e -> deleteGoal(goal));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openGoalDialog(Goal goal) {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        TextField goalNameField = new TextField("Tên mục tiêu");
        BigDecimalField targetAmountField = new BigDecimalField("Số tiền mục tiêu");
        BigDecimalField currentAmountField = new BigDecimalField("Số tiền hiện tại");
        DatePicker deadlineField = new DatePicker("Hạn chót");
        DatePicker startDateField = new DatePicker("Ngày bắt đầu");

        Select<User> userField = new Select<>();
        userField.setLabel("Người dùng");
        userField.setItems(userRepository.findAll());
        userField.setItemLabelGenerator(User::getUsername);

        Select<Category> categoryField = new Select<>();
        categoryField.setLabel("Danh mục");
        categoryField.setItems(categoryRepository.findAll());
        categoryField.setItemLabelGenerator(Category::getCategoryName);

        Select<Wallet> walletField = new Select<>();
        walletField.setLabel("Ví");
        walletField.setItems(walletRepository.findAll());
        walletField.setItemLabelGenerator(Wallet::getWalletName);

        binder.bind(goalNameField, Goal::getGoalName, Goal::setGoalName);
        binder.bind(targetAmountField, Goal::getTargetAmount, Goal::setTargetAmount);
        binder.bind(currentAmountField, Goal::getCurrentAmount, Goal::setCurrentAmount);
        binder.bind(deadlineField, Goal::getDeadline, Goal::setDeadline);
        binder.bind(startDateField, Goal::getStartDate, Goal::setStartDate);
        binder.bind(userField, Goal::getUser, Goal::setUser);
        binder.bind(categoryField, Goal::getCategory, Goal::setCategory);
        binder.bind(walletField, Goal::getWallet, Goal::setWallet);

        binder.setBean(goal);

        form.add(goalNameField, targetAmountField, currentAmountField, deadlineField, startDateField, userField, categoryField, walletField);

        Button saveButton = new Button("Lưu", e -> saveGoal(goal, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveGoal(Goal goal, Dialog dialog) {
        try {
            goalRepository.save(goal);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteGoal(Goal goal) {
        goalRepository.delete(goal);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(goalRepository.findAll());
    }
}