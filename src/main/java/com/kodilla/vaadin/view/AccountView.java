package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.AccountTransactionDto;
import com.kodilla.vaadin.domain.CryptoRatesDto;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CryptoRatesService;
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
        Grid<AccountTransactionDto> accountGrid = new Grid<>(AccountTransactionDto.class);

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
        depositValue.setSuffixComponent(depositSuffix);
        Button addDeposit = new Button("Add money");
        addDeposit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addDeposit.addClickListener(click -> {
            accountService.addDeposit(depositValue.getValue());
            accountGrid.setItems(accountService.getAllDeposits());
            accountBalanceValue.setText(getBalance());
            Notification notification = Notification
                    .show(depositValue.getValue() + " zł added to the account");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);
            depositValue.clear();
        });
        depositLayout.add(depositValue, addDeposit);
        depositLayout.setAlignItems(Alignment.BASELINE);

        H3 depositHistory = new H3("Deposit history");
        accountGrid.setItems(accountService.getAllDeposits());
        accountGrid.setColumns("depositId", "depositDate", "depositValue");

        accountLayout.add(accountBalance,accountBalanceValue, deposit, depositLayout,depositHistory, accountGrid);
        add(accountLayout);
    }

    public String getBalance() {

        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }

    public void refresh() {
        getBalance();

    }

}
