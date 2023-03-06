package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.AccountDepositDto;
import com.kodilla.vaadin.service.AccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.RoundingMode;


@Route(value = "account", layout = MainLayout.class)
@PageTitle("Account | Savings App")
public class AccountView extends VerticalLayout {


    private final AccountService accountService = AccountService.getInstance();

    public AccountView() {
        VerticalLayout accountLayout = new VerticalLayout();
        HorizontalLayout depositLayout = new HorizontalLayout();
        HorizontalLayout withdrawLayout = new HorizontalLayout();
        Grid<AccountDepositDto> accountGrid = new Grid<>(AccountDepositDto.class);

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 deposit = new H3("Deposit money on your account");
        BigDecimalField depositValue = new BigDecimalField("Deposit");
        depositValue.setPlaceholder("Enter value");
        depositValue.setWidth("800");
        depositValue.setClearButtonVisible(true);
        Div depositSuffix = new Div();
        depositSuffix.setText("zł");
        depositValue.setSuffixComponent(depositSuffix);
        Button addDeposit = new Button("Add money");
        addDeposit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addDeposit.addClickListener(click -> {
            accountService.addDeposit(depositValue.getValue());
            refresh(accountBalanceValue, accountGrid);
            Notification notification = Notification
                    .show(depositValue.getValue() + " zł added to the account");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);
            depositValue.clear();
        });

        BigDecimalField withdrawValue = new BigDecimalField("Withdraw");
        withdrawValue.setPlaceholder("Enter value");
        withdrawValue.setWidth("800");
        withdrawValue.setClearButtonVisible(true);
        Div withdrawSuffix = new Div();
        withdrawSuffix.setText("zł");
        withdrawValue.setSuffixComponent(withdrawSuffix);
        Button withdrawDeposit = new Button("Withdraw money");
        withdrawDeposit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        withdrawDeposit.addClickListener(click -> {
            if (withdrawValue.getValue().compareTo(accountService.getBalance()) > 0) {
                Notification notification = Notification
                        .show("Not enough money on account");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            } else {
                accountService.addDeposit(withdrawValue.getValue().negate());
                refresh(accountBalanceValue, accountGrid);
                Notification notification = Notification
                        .show(withdrawValue.getValue() + " zł withdraw from account");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }
            withdrawValue.clear();
        });
        depositLayout.add(depositValue, addDeposit);
        withdrawLayout.add(withdrawValue, withdrawDeposit);
        depositLayout.setAlignItems(Alignment.BASELINE);
        withdrawLayout.setAlignItems(Alignment.BASELINE);

        H3 depositHistory = new H3("Deposit history");
        accountGrid.setItems(accountService.getAllDeposits());
        accountGrid.setColumns("depositDate", "depositValue", "depositType");

        accountLayout.add(accountBalance,accountBalanceValue, deposit, depositLayout, withdrawLayout, depositHistory, accountGrid);
        add(accountLayout);
    }

    public String getBalance() {

        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }

    public void refresh(Label accountBalanceValue, Grid<AccountDepositDto> accountGrid) {
        accountBalanceValue.setText(getBalance());
        accountGrid.getDataProvider().refreshAll();
        accountGrid.setItems(accountService.getAllDeposits());
    }

}
