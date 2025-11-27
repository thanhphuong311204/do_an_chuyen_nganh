package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Expense;
import com.appQLCT.AppQLCT.repository.core.ExpenseRepository;
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

@Route(value = "expenses", layout = MainLayout.class)
public class ExpenseView extends VerticalLayout {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private Grid<Expense> grid = new Grid<>(Expense.class);
    private Binder<Expense> binder = new Binder<>(Expense.class);

    @Autowired
    public ExpenseView(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

        add(new H2("Quản lý Chi tiêu"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openExpenseDialog(new Expense()));
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "amount", "note", "createAt");
        grid.addComponentColumn(expense -> {
            Button editButton = new Button("Sửa", e -> openExpenseDialog(expense));
            Button deleteButton = new Button("Xóa", e -> deleteExpense(expense));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openExpenseDialog(Expense expense) {
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

        BigDecimalField amountField = new BigDecimalField("Số tiền");
        TextField noteField = new TextField("Ghi chú");
        DatePicker createAtField = new DatePicker("Ngày tạo");

        binder.bind(userField, Expense::getUser, Expense::setUser);
        binder.bind(categoryField, Expense::getCategory, Expense::setCategory);
        binder.bind(walletField, Expense::getWallet, Expense::setWallet);
        binder.bind(amountField, Expense::getAmount, Expense::setAmount);
        binder.bind(noteField, Expense::getNote, Expense::setNote);
        binder.bind(createAtField, Expense::getCreateAt, Expense::setCreateAt);

        binder.setBean(expense);

        form.add(userField, categoryField, walletField, amountField, noteField, createAtField);

        Button saveButton = new Button("Lưu", e -> saveExpense(expense, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveExpense(Expense expense, Dialog dialog) {
        try {
            expenseRepository.save(expense);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(expenseRepository.findAll());
    }
}