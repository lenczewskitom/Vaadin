package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.CurrencyTransactionDto;
import com.kodilla.vaadin.domain.enums.Currency;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CurrencyService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Route(value = "currency", layout = MainLayout.class)
@PageTitle("Currency | Savings App")
public class CurrencyView extends HorizontalLayout {

    private AccountService accountService= AccountService.getInstance();
    private CurrencyService currencyService = CurrencyService.getInstance();

    public CurrencyView() {

        VerticalLayout currencyLayout = new VerticalLayout();
        VerticalLayout exchangeLayout = new VerticalLayout();
        VerticalLayout balanceLayout = new VerticalLayout();
        VerticalLayout ratesLayout = new VerticalLayout();
        HorizontalLayout topLayout = new HorizontalLayout();
        HorizontalLayout amountLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        Grid<CurrencyTransactionDto> currencyGrid = new Grid<>(CurrencyTransactionDto.class);

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 exchangeCurrency = new H3("Exchange Currency");
        BigDecimalField currencyAmount = new BigDecimalField("Amount");
        ComboBox<Currency> currency = new ComboBox<>("Currency");
        currency.setItems(Currency.values());
        amountLayout.add(currencyAmount, currency);
        Button buy = new Button("Buy");
        buy.addClickListener(click -> {
            BigDecimal accountValue = currencyAmount.getValue().multiply(currencyService.getExchangeRate(currency.getValue()));
            if (accountService.getBalance().compareTo(accountValue) < 0) {
                Notification notification = Notification
                        .show("Not enugh money on the account");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            } else {
                currencyService.buyCurrency(accountValue, currency.getValue(),currencyAmount.getValue());
                currencyGrid.setItems(currencyService.getAllTransactions());
                accountBalanceValue.setText(getBalance());
                UI.getCurrent().getPage().reload();
                Notification notification = Notification
                        .show(currencyAmount.getValue() + " " + currency.getValue() + " added to the account");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }
            currencyAmount.clear();
            currency.clear();
        });

        Button sell = new Button("Sell");
        sell.addClickListener(click -> {
            if (currencyService.getCurrencyBalance(currency.getValue()).getBalance().compareTo(currencyAmount.getValue()) < 0) {
                Notification notification = Notification
                        .show("Not enugh currency on the account");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            } else {
                BigDecimal accountValue = currencyAmount.getValue().multiply(currencyService.getExchangeRate(currency.getValue()));
                currencyService.sellCurrency(accountValue, currency.getValue(),currencyAmount.getValue());
                currencyGrid.setItems(currencyService.getAllTransactions());
                accountBalanceValue.setText(getBalance());
                UI.getCurrent().getPage().reload();
                Notification notification = Notification
                        .show(currencyAmount.getValue() + " " + currency.getValue() + " sold from the account");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }
            currencyAmount.clear();
            currency.clear();
        });
        buttonsLayout.add(buy, sell);
        exchangeLayout.add(accountBalance, accountBalanceValue, exchangeCurrency, amountLayout, buttonsLayout);

        H3 currencyBalance = new H3("Currencies balance");
        Label eur = new Label(Currency.EUR + " - " + currencyService.getCurrencyBalance(Currency.EUR).getBalance().setScale(2, RoundingMode.CEILING));
        Label usd = new Label(Currency.USD + " - " + currencyService.getCurrencyBalance(Currency.USD).getBalance().setScale(2, RoundingMode.CEILING));
        Label gbp = new Label(Currency.GBP + " - " + currencyService.getCurrencyBalance(Currency.GBP).getBalance().setScale(2, RoundingMode.CEILING));
        Label chf = new Label(Currency.CHF + " - " + currencyService.getCurrencyBalance(Currency.CHF).getBalance().setScale(2, RoundingMode.CEILING));
        Label cny = new Label(Currency.CNY + " - " + currencyService.getCurrencyBalance(Currency.CNY).getBalance().setScale(2, RoundingMode.CEILING));
        balanceLayout.add(currencyBalance, eur, usd, gbp, chf, cny);

        H3 rates = new H3("Actual rates");
        Label euro = new Label(Currency.EUR + " - " + currencyService.getExchangeRate( Currency.EUR));
        Label dolar = new Label(Currency.USD + " - " + currencyService.getExchangeRate(Currency.USD));
        Label funt = new Label(Currency.GBP + " - " + currencyService.getExchangeRate(Currency.GBP));
        Label frank = new Label(Currency.CHF + " - " + currencyService.getExchangeRate(Currency.CHF));
        Label juan = new Label(Currency.CNY + " - " + currencyService.getExchangeRate(Currency.CNY));
        ratesLayout.add(rates, euro, dolar, funt, frank, juan);
        topLayout.add(exchangeLayout, balanceLayout, ratesLayout);

        H3 depositHistory = new H3("Transactions history");
        currencyGrid.setItems(currencyService.getAllTransactions());
        currencyGrid.setColumns("transactionId", "transactionDate", "transactionAccountValue", "currencyCode", "transactionCurrencyValue");

        currencyLayout.add(topLayout, depositHistory, currencyGrid);
        add(currencyLayout);
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }

}
