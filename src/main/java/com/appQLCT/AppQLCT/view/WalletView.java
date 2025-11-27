package com.appQLCT.AppQLCT.view;

import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.repository.core.WalletRepository;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import com.appQLCT.AppQLCT.entity.authentic.User;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "wallets", layout = MainLayout.class)
public class WalletView extends VerticalLayout {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private Grid<Wallet> grid = new Grid<>(Wallet.class);
    private Binder<Wallet> binder = new Binder<>(Wallet.class);

    @Autowired
    public WalletView(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;

        add(new H2("Quản lý Ví"));

        configureGrid();
        add(grid);

        Button addButton = new Button("Thêm mới", e -> openWalletDialog(new Wallet()));
        add(addButton);

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "walletName", "type", "balance");
        grid.addComponentColumn(wallet -> {
            Button editButton = new Button("Sửa", e -> openWalletDialog(wallet));
            Button deleteButton = new Button("Xóa", e -> deleteWallet(wallet));
            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void openWalletDialog(Wallet wallet) {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        TextField walletNameField = new TextField("Tên ví");
        TextField typeField = new TextField("Loại");
        BigDecimalField balanceField = new BigDecimalField("Số dư");

        Select<User> userField = new Select<>();
        userField.setLabel("Người dùng");
        userField.setItems(userRepository.findAll());
        userField.setItemLabelGenerator(User::getUsername);

        binder.bind(walletNameField, Wallet::getWalletName, Wallet::setWalletName);
        binder.bind(typeField, Wallet::getType, Wallet::setType);
        binder.bind(balanceField, Wallet::getBalance, Wallet::setBalance);
        binder.bind(userField, Wallet::getUser, Wallet::setUser);

        binder.setBean(wallet);

        form.add(walletNameField, typeField, balanceField, userField);

        Button saveButton = new Button("Lưu", e -> saveWallet(wallet, dialog));
        Button cancelButton = new Button("Hủy", e -> dialog.close());

        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void saveWallet(Wallet wallet, Dialog dialog) {
        try {
            walletRepository.save(wallet);
            updateList();
            dialog.close();
            Notification.show("Lưu thành công");
        } catch (Exception e) {
            Notification.show("Lỗi: " + e.getMessage());
        }
    }

    private void deleteWallet(Wallet wallet) {
        walletRepository.delete(wallet);
        updateList();
        Notification.show("Xóa thành công");
    }

    private void updateList() {
        grid.setItems(walletRepository.findAll());
    }
}