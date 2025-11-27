package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.RecurringTransaction;
import com.appQLCT.AppQLCT.repository.core.RecurringTransactionRepository;
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

@Route(value = "recurring-transactions", layout = MainLayout.class)
public class RecurringTransactionView extends VerticalLayout {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;
    private Grid<RecurringTransaction> grid = new Grid<>(RecurringTransaction.class);
    private Binder<RecurringTransaction> binder = new Binder<>(RecurringTransaction.class);

    @Autowired
    public RecurringTransactionView(RecurringTransactionRepository recurringTransactionRepository, UserRepository userRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;

        add(new H2("Quản lý Giao dịch định kỳ"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openRecurringTransactionDialog(new RecurringTransaction()));
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "amount", "note", "frequency", "nextDate");
        grid.addComponentColumn(recurringTransaction -> {
            Button editButton = new Button("Sửa", e -> openRecurringTransactionDialog(recurringTransaction));
            Button deleteButton = new Button("Xóa", e -> deleteRecurringTransaction(recurringTransaction));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openRecurringTransactionDialog(RecurringTransaction recurringTransaction) {
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

        TextField categoryNameField = new TextField("Tên danh mục");
        BigDecimalField amountField = new BigDecimalField("Số tiền");
        TextField noteField = new TextField("Ghi chú");
        TextField frequencyField = new TextField("Tần suất");
        DatePicker nextDateField = new DatePicker("Ngày tiếp theo");

        binder.bind(userField, RecurringTransaction::getUser, RecurringTransaction::setUser);
        binder.bind(categoryField, RecurringTransaction::getCategory, RecurringTransaction::setCategory);
        binder.bind(walletField, RecurringTransaction::getWallet, RecurringTransaction::setWallet);
        binder.bind(categoryNameField, RecurringTransaction::getCategoryName, RecurringTransaction::setCategoryName);
        binder.bind(amountField, RecurringTransaction::getAmount, RecurringTransaction::setAmount);
        binder.bind(noteField, RecurringTransaction::getNote, RecurringTransaction::setNote);
        binder.bind(frequencyField, RecurringTransaction::getFrequency, RecurringTransaction::setFrequency);
        binder.bind(nextDateField, RecurringTransaction::getNextDate, RecurringTransaction::setNextDate);

        binder.setBean(recurringTransaction);

        form.add(userField, categoryField, walletField, categoryNameField, amountField, noteField, frequencyField, nextDateField);

        Button saveButton = new Button("Lưu", e -> saveRecurringTransaction(recurringTransaction, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveRecurringTransaction(RecurringTransaction recurringTransaction, Dialog dialog) {
        try {
            recurringTransactionRepository.save(recurringTransaction);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteRecurringTransaction(RecurringTransaction recurringTransaction) {
        recurringTransactionRepository.delete(recurringTransaction);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(recurringTransactionRepository.findAll());
    }
}