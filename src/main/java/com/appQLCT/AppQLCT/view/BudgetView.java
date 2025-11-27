package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Budget;
import com.appQLCT.AppQLCT.repository.core.BudgetRepository;
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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "budgets", layout = MainLayout.class)
public class BudgetView extends VerticalLayout {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private Grid<Budget> grid = new Grid<>(Budget.class);
    private Binder<Budget> binder = new Binder<>(Budget.class);

    @Autowired
    public BudgetView(BudgetRepository budgetRepository, UserRepository userRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

        add(new H2("Quản lý Ngân sách"));

        configureGrid();
        VerticalLayout gridContainer = new VerticalLayout(grid);
        gridContainer.addClassName("grid-container");
        add(gridContainer);

        Button addButton = new Button("Thêm mới", e -> openBudgetDialog(new Budget()));
        addButton.addClassName("add-button");
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "amountLimit", "spentAmount", "startDate", "endDate");
        grid.addComponentColumn(budget -> {
            Button editButton = new Button("Sửa", e -> openBudgetDialog(budget));
            Button deleteButton = new Button("Xóa", e -> deleteBudget(budget));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openBudgetDialog(Budget budget) {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

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

        BigDecimalField amountLimitField = new BigDecimalField("Giới hạn số tiền");
        BigDecimalField spentAmountField = new BigDecimalField("Số tiền đã chi");
        DatePicker startDateField = new DatePicker("Ngày bắt đầu");
        DatePicker endDateField = new DatePicker("Ngày kết thúc");

        binder.bind(userField, Budget::getUser, Budget::setUser);
        binder.bind(categoryField, Budget::getCategory, Budget::setCategory);
        binder.bind(walletField, Budget::getWallet, Budget::setWallet);
        binder.bind(amountLimitField, Budget::getAmountLimit, Budget::setAmountLimit);
        binder.bind(spentAmountField, Budget::getSpentAmount, Budget::setSpentAmount);
        binder.bind(startDateField, Budget::getStartDate, Budget::setStartDate);
        binder.bind(endDateField, Budget::getEndDate, Budget::setEndDate);

        binder.setBean(budget);

        form.add(userField, categoryField, walletField, amountLimitField, spentAmountField, startDateField, endDateField);

        Button saveButton = new Button("Lưu", e -> saveBudget(budget, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveBudget(Budget budget, Dialog dialog) {
        try {
            budgetRepository.save(budget);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteBudget(Budget budget) {
        budgetRepository.delete(budget);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(budgetRepository.findAll());
    }
}