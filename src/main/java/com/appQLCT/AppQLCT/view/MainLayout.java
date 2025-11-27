package com.appQLCT.AppQLCT.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Quản lý Chi Tiêu");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink homeLink = new RouterLink("Trang chủ", MainView.class);
        homeLink.setHighlightCondition(HighlightConditions.sameLocation());
        homeLink.addClassName("sidebar-link");

        RouterLink usersLink = new RouterLink("Quản lý Người dùng", UserView.class);
        usersLink.addClassName("sidebar-link");
        RouterLink budgetsLink = new RouterLink("Quản lý Ngân sách", BudgetView.class);
        budgetsLink.addClassName("sidebar-link");
        RouterLink categoriesLink = new RouterLink("Quản lý Danh mục", CategoryView.class);
        categoriesLink.addClassName("sidebar-link");
        RouterLink expensesLink = new RouterLink("Quản lý Chi tiêu", ExpenseView.class);
        expensesLink.addClassName("sidebar-link");
        RouterLink goalsLink = new RouterLink("Quản lý Mục tiêu", GoalView.class);
        goalsLink.addClassName("sidebar-link");
        RouterLink incomesLink = new RouterLink("Quản lý Thu nhập", IncomeView.class);
        incomesLink.addClassName("sidebar-link");
        RouterLink walletsLink = new RouterLink("Quản lý Ví", WalletView.class);
        walletsLink.addClassName("sidebar-link");
        RouterLink recurringLink = new RouterLink("Quản lý Giao dịch định kỳ", RecurringTransactionView.class);
        recurringLink.addClassName("sidebar-link");

        addToDrawer(new VerticalLayout(
            homeLink,
            usersLink,
            budgetsLink,
            categoriesLink,
            expensesLink,
            goalsLink,
            incomesLink,
            walletsLink,
            recurringLink
        ));
    }
}