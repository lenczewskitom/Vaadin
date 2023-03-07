package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.CurrencyBalanceDto;
import com.kodilla.vaadin.domain.CurrencyOrderDto;
import com.kodilla.vaadin.domain.CurrencyRatesDto;
import com.kodilla.vaadin.domain.CurrencyTransactionDto;
import com.kodilla.vaadin.domain.enums.Currency;
import com.kodilla.vaadin.domain.enums.Order;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CurrencyService;
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

import javax.swing.*;
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
        VerticalLayout ordersLayout = new VerticalLayout();
        HorizontalLayout topLayout = new HorizontalLayout();
        HorizontalLayout amountLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        HorizontalLayout orderAmountLayout = new HorizontalLayout();
        HorizontalLayout orderRateLayout = new HorizontalLayout();
        HorizontalLayout orderButtonsLayout = new HorizontalLayout();

        Grid<CurrencyTransactionDto> currencyGrid = new Grid<>(CurrencyTransactionDto.class);
        Grid<CurrencyBalanceDto> balanceGrid = new Grid<>(CurrencyBalanceDto.class, false);
        balanceGrid.addColumn(CurrencyBalanceDto::getCurrencyCode).setHeader("Currency");
        balanceGrid.addColumn(CurrencyBalanceDto::getBalance).setHeader("Balance");
        Grid<CurrencyRatesDto> ratesGrid = new Grid<>(CurrencyRatesDto.class, false);
        ratesGrid.addColumn(CurrencyRatesDto::getCurrencyCode).setHeader("Currency");
        ratesGrid.addColumn(CurrencyRatesDto::getLastRate).setHeader("Rate");
        Grid<CurrencyOrderDto> ordersGrid = new Grid<>(CurrencyOrderDto.class,false);
        ordersGrid.addColumn(CurrencyOrderDto::getCurrencyOrderDate).setHeader("Date").setAutoWidth(true);
        ordersGrid.addColumn(CurrencyOrderDto::getCurrencyCode).setHeader("Currency");
        ordersGrid.addColumn(CurrencyOrderDto::getOrderCurrencyValue).setHeader("Amount");
        ordersGrid.addColumn(CurrencyOrderDto::getCurrencyRate).setHeader("Rate");
        ordersGrid.addColumn(CurrencyOrderDto::getOperationType).setHeader("Type");

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 exchangeCurrency = new H3("Exchange Currency");
        BigDecimalField currencyAmount = new BigDecimalField("Amount");
        currencyAmount.setPlaceholder("Enter value");
        ComboBox<Currency> currency = new ComboBox<>("Currency");
        currency.setItems(Currency.values());
        amountLayout.add(currencyAmount, currency);
        Button buy = new Button("Buy");
        buy.addClickListener(click -> {
            if (currencyAmount.isEmpty() || currency.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                BigDecimal accountValue = currencyAmount.getValue().multiply(currencyService.getExchangeRate(currency.getValue()));
                if (accountService.getBalance().compareTo(accountValue) < 0) {
                    sendErrorNotification("Not enough money on the account");
                } else {
                    currencyService.buyCurrency(accountValue, currency.getValue(),currencyAmount.getValue());
                    refresh(currencyGrid, balanceGrid, ratesGrid, accountBalanceValue);
                    Notification notification = Notification
                            .show(currencyAmount.getValue() + " " + currency.getValue() + " added to the account");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
                exchangeClear(currencyAmount, currency);
            }
        });

        Button sell = new Button("Sell");
        sell.addClickListener(click -> {
            if (currencyAmount.isEmpty() || currency.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                if (currencyService.getCurrencyBalance(currency.getValue()).getBalance().compareTo(currencyAmount.getValue()) < 0) {
                    sendErrorNotification("Not enough currency on the account");
                } else {
                    BigDecimal accountValue = currencyAmount.getValue().multiply(currencyService.getExchangeRate(currency.getValue()));
                    currencyService.sellCurrency(accountValue, currency.getValue(),currencyAmount.getValue());
                    refresh(currencyGrid, balanceGrid, ratesGrid, accountBalanceValue);
                    Notification notification = Notification
                            .show(currencyAmount.getValue() + " " + currency.getValue() + " sold from the account");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
                exchangeClear(currencyAmount, currency);
            }
        });
        buttonsLayout.add(buy, sell);

        H3 orderCurrency = new H3("Order Currency");
        BigDecimalField orderCurrencyAmount = new BigDecimalField("Amount");
        orderCurrencyAmount.setPlaceholder("Enter value");
        ComboBox<Currency> orderCombobox = new ComboBox<>("Currency");
        orderCombobox.setItems(Currency.values());
        orderAmountLayout.add(orderCurrencyAmount, orderCombobox);
        BigDecimalField orderCurrencyRate = new BigDecimalField("Rate");
        orderCurrencyRate.setPlaceholder("Enter value");
        Button orderBuy = new Button("Buy");
        orderBuy.addClickListener(click -> {
            if (orderCurrencyAmount.isEmpty() || orderCombobox.isEmpty() || orderCurrencyRate.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                BigDecimal currencyValue  = orderCurrencyAmount.getValue().multiply(currencyService.getExchangeRate(orderCombobox.getValue()));
                if (accountService.getBalance().compareTo(currencyValue.add(currencyService.getAllOrdersAccountValue())) < 0) {
                    sendErrorNotification("Not enough money on the account");
                } else {
                    addOrder(orderCurrencyAmount,orderCombobox, orderCurrencyRate, Order.BUY, ordersGrid);
                }
                orderClear(orderCurrencyAmount,orderCombobox, orderCurrencyRate);
            }
        });

        Button orderSell = new Button("Sell");
        orderSell.addClickListener(click -> {
            if (orderCurrencyAmount.isEmpty() || orderCombobox.isEmpty() || orderCurrencyRate.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                if (currencyService.getCurrencyBalance(orderCombobox.getValue()).getBalance()
                        .compareTo(orderCurrencyAmount.getValue().add(currencyService.getAllOrdersCurrencyValue(orderCombobox.getValue()))) < 0) {
                    sendErrorNotification("Not enough currency on the account");
                } else {
                    addOrder(orderCurrencyAmount,orderCombobox, orderCurrencyRate, Order.SELL, ordersGrid);
                }
                orderClear(orderCurrencyAmount,orderCombobox, orderCurrencyRate);
            }
        });

        Button deleteOrder = new Button("Delete order");
        deleteOrder.addClickListener(click -> {
            if (ordersGrid.getSelectionModel().getFirstSelectedItem().isEmpty()) {
                sendErrorNotification("Select order to delete");
            } else {
                currencyService.deleteCurrencyOrder(ordersGrid.getSelectionModel().getFirstSelectedItem().get().getCurrencyOrderId());
                Notification notification = Notification
                        .show("Order removed");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
                ordersGrid.setItems(currencyService.getAllCurrencyOrdersList());
                ordersGrid.getDataProvider().refreshAll();
            }
        });

        orderButtonsLayout.add(orderBuy, orderSell, deleteOrder);
        exchangeLayout.add(accountBalance, accountBalanceValue, exchangeCurrency, amountLayout, buttonsLayout, orderCurrency, orderAmountLayout,
                orderCurrencyRate, orderButtonsLayout);

        H3 currencyBalance = new H3("Actual balance");
        balanceGrid.setItems(currencyService.getAllCurrencyBalanceList());
        balanceGrid.setHeightByRows(true);
        balanceLayout.add(currencyBalance, balanceGrid);

        H3 rates = new H3("Actual rates");
        ratesGrid.setItems(currencyService.getAllCurrencyRatesList());
        ratesGrid.setHeightByRows(true);
        ratesLayout.add(rates, ratesGrid);

        H3 orders = new H3("Actual orders");
        ordersGrid.setItems(currencyService.getAllCurrencyOrdersList());
        ordersGrid.setHeightByRows(true);

        ordersLayout.add(orders, ordersGrid);
        topLayout.add(exchangeLayout, balanceLayout, ratesLayout, ordersLayout);

        H3 depositHistory = new H3("Transactions history");
        currencyGrid.setItems(currencyService.getAllTransactions());
        currencyGrid.setColumns("transactionDate", "transactionAccountValue", "currencyCode", "transactionCurrencyValue");

        currencyLayout.add(topLayout, depositHistory, currencyGrid);
        add(currencyLayout);
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }

    public void addOrder(BigDecimalField orderCurrencyAmount, ComboBox<Currency> orderCombobox, BigDecimalField orderCurrencyRate,
                         Order orderType, Grid<CurrencyOrderDto> ordersGrid) {
        currencyService.addCurrencyOrder(orderCurrencyAmount.getValue(), orderCombobox.getValue(), orderCurrencyRate.getValue(), orderType);
        ordersGrid.setItems(currencyService.getAllCurrencyOrdersList());
        ordersGrid.getDataProvider().refreshAll();
        Notification notification = Notification
                .show("Added new currency order");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void exchangeClear(BigDecimalField currencyAmount, ComboBox<Currency> currency) {
        currencyAmount.clear();
        currency.clear();
    }

    public void orderClear(BigDecimalField orderCurrencyAmount, ComboBox<Currency> orderCombobox, BigDecimalField orderCurrencyRate) {
        orderCurrencyAmount.clear();
        orderCombobox.clear();
        orderCurrencyRate.clear();
    }

    public void sendErrorNotification(String info) {
        Notification notification = Notification
                .show(info);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void refresh(Grid<CurrencyTransactionDto> currencyGrid, Grid<CurrencyBalanceDto> balanceGrid, Grid<CurrencyRatesDto> ratesGrid, Label accountBalanceValue) {
        accountBalanceValue.setText(getBalance());
        currencyGrid.setItems(currencyService.getAllTransactions());
        currencyGrid.getDataProvider().refreshAll();
        balanceGrid.setItems(currencyService.getAllCurrencyBalanceList());
        balanceGrid.getDataProvider().refreshAll();
        ratesGrid.setItems(currencyService.getAllCurrencyRatesList());
        ratesGrid.getDataProvider().refreshAll();
    }
}
