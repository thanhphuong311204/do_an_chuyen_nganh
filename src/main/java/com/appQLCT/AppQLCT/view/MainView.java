package com.appQLCT.AppQLCT.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(layout = MainLayout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Background.BASE);

        H1 title = new H1("Tổng quan hệ thống");
        title.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.Margin.Bottom.LARGE);

        FlexLayout cards = new FlexLayout();
        cards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cards.setWidthFull();

        VerticalLayout userCard = createCard("Người dùng", "120", "text-primary");
        VerticalLayout expenseCard = createCard("Tổng chi", "45,000,000₫", "text-danger");
        VerticalLayout incomeCard = createCard("Tổng thu", "68,000,000₫", "text-success");
        VerticalLayout transactionCard = createCard("Giao dịch", "312", "text-warning");

        cards.add(userCard, expenseCard, incomeCard, transactionCard);

        Paragraph welcome = new Paragraph("Chào mừng bạn đến hệ thống quản lý chi tiêu!");
        welcome.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.TextAlignment.CENTER, LumoUtility.Margin.Top.LARGE);

        add(title, cards, welcome);
    }

    private VerticalLayout createCard(String title, String value, String colorClass) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("my-card");
        card.setWidth("250px");

        H3 cardTitle = new H3(title);
        cardTitle.addClassNames(LumoUtility.TextColor.BODY, LumoUtility.Margin.Bottom.SMALL);

        Paragraph cardValue = new Paragraph(value);
        cardValue.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.FontWeight.BOLD, colorClass, LumoUtility.Margin.NONE);

        card.add(cardTitle, cardValue);
        return card;
    }
}