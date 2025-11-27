package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Income;
import com.appQLCT.AppQLCT.repository.core.IncomeRepository;
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

@Route(value = "incomes", layout = MainLayout.class)
public class IncomeView extends VerticalLayout {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private Grid<Income> grid = new Grid<>(Income.class);
    private Binder<Income> binder = new Binder<>(Income.class);

    @Autowired
    public IncomeView(IncomeRepository incomeRepository, UserRepository userRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

        add(new H2("Quản lý Thu nhập"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openIncomeDialog(new Income()));
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "amount", "note", "incomeDate");
        grid.addComponentColumn(income -> {
            Button editButton = new Button("Sửa", e -> openIncomeDialog(income));
            Button deleteButton = new Button("Xóa", e -> deleteIncome(income));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openIncomeDialog(Income income) {
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
        DatePicker incomeDateField = new DatePicker("Ngày thu nhập");

        binder.bind(userField, Income::getUser, Income::setUser);
        binder.bind(categoryField, Income::getCategory, Income::setCategory);
        binder.bind(walletField, Income::getWallet, Income::setWallet);
        binder.bind(amountField, Income::getAmount, Income::setAmount);
        binder.bind(noteField, Income::getNote, Income::setNote);
        binder.bind(incomeDateField, Income::getIncomeDate, Income::setIncomeDate);

        binder.setBean(income);

        form.add(userField, categoryField, walletField, amountField, noteField, incomeDateField);

        Button saveButton = new Button("Lưu", e -> saveIncome(income, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveIncome(Income income, Dialog dialog) {
        try {
            incomeRepository.save(income);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteIncome(Income income) {
        incomeRepository.delete(income);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(incomeRepository.findAll());
    }
}