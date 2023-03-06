package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.*;
import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import com.kodilla.vaadin.domain.enums.Order;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CryptoService;
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

@Route(value = "crypto", layout = MainLayout.class)
@PageTitle("Crypto | Savings App")
public class CryptoView extends HorizontalLayout {

    private final AccountService accountService= AccountService.getInstance();
    private final CryptoService cryptoService = CryptoService.getInstance();

    public CryptoView() {
        VerticalLayout cryptoLayout = new VerticalLayout();
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

        Grid<CryptoTransactionDto> cryptoGrid = new Grid<>(CryptoTransactionDto.class);
        Grid<CryptoBalanceDto> balanceGrid = new Grid<>(CryptoBalanceDto.class, false);
        balanceGrid.addColumn(CryptoBalanceDto::getCryptocurrencyCode).setHeader("Cryptocurrency").setAutoWidth(true);
        balanceGrid.addColumn(CryptoBalanceDto::getBalance).setHeader("Balance");
        Grid<CryptoRatesDto> ratesGrid = new Grid<>(CryptoRatesDto.class, false);
        ratesGrid.addColumn(CryptoRatesDto::getCryptocurrencyCode).setHeader("Cryptocurrency").setAutoWidth(true);
        ratesGrid.addColumn(CryptoRatesDto::getLastRate).setHeader("Rate").setAutoWidth(true);
        Grid<CryptoOrderDto> ordersGrid = new Grid<>(CryptoOrderDto.class,false);
        ordersGrid.addColumn(CryptoOrderDto::getCryptoOrderDate).setHeader("Date").setAutoWidth(true);
        ordersGrid.addColumn(CryptoOrderDto::getCryptoCode).setHeader("Cryptocurrency").setAutoWidth(true);
        ordersGrid.addColumn(CryptoOrderDto::getOrderCryptoValue).setHeader("Amount");
        ordersGrid.addColumn(CryptoOrderDto::getCryptoRate).setHeader("Rate").setAutoWidth(true);
        ordersGrid.addColumn(CryptoOrderDto::getOperationType).setHeader("Type");

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 exchangeCryptoCurrency = new H3("Exchange Cryptocurrency");
        BigDecimalField cryptocurrencyAmount = new BigDecimalField("Amount");
        cryptocurrencyAmount.setPlaceholder("Enter value");
        ComboBox<CryptoCurrency> cryptocurrency = new ComboBox<>("Cryptocurrency");
        cryptocurrency.setItems(CryptoCurrency.values());
        amountLayout.add(cryptocurrencyAmount, cryptocurrency);
        Button buy = new Button("Buy");
        buy.addClickListener(click -> {
            if (cryptocurrencyAmount.isEmpty() || cryptocurrency.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                BigDecimal accountValue = cryptocurrencyAmount.getValue().multiply(cryptoService.getCryptoRate(cryptocurrency.getValue()));
                if (accountService.getBalance().compareTo(accountValue) < 0) {
                    sendErrorNotification("Not enough money on the account");
                } else {
                    cryptoService.buyCryptocurrency(accountValue, cryptocurrency.getValue(), cryptocurrencyAmount.getValue());
                    refresh(cryptoGrid, balanceGrid, ratesGrid, accountBalanceValue);
                    Notification notification = Notification
                            .show(cryptocurrencyAmount.getValue() + " " + cryptocurrency.getValue() + " added to the account");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
                exchangeClear(cryptocurrencyAmount, cryptocurrency);
            }
        });

        Button sell = new Button("Sell");
        sell.addClickListener(click -> {
            if (cryptocurrencyAmount.isEmpty() || cryptocurrency.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                if (cryptoService.getCryptoBalance(cryptocurrency.getValue()).getBalance().compareTo(cryptocurrencyAmount.getValue()) < 0) {
                    sendErrorNotification("Not enough currency on the account");
                } else {
                    BigDecimal accountValue = cryptocurrencyAmount.getValue().multiply(cryptoService.getCryptoRate(cryptocurrency.getValue()));
                    cryptoService.sellCurrency(accountValue, cryptocurrency.getValue(),cryptocurrencyAmount.getValue());
                    refresh(cryptoGrid, balanceGrid, ratesGrid, accountBalanceValue);
                    Notification notification = Notification
                            .show(cryptocurrencyAmount.getValue() + " " + cryptocurrency.getValue() + " sold from the account");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
                exchangeClear(cryptocurrencyAmount, cryptocurrency);
            }
        });

        buttonsLayout.add(buy, sell);

        H3 orderCrypto = new H3("Order Cryptocurrency");
        BigDecimalField orderCryptoAmount = new BigDecimalField("Amount");
        orderCryptoAmount.setPlaceholder("Enter value");
        ComboBox<CryptoCurrency> orderCombobox = new ComboBox<>("Cryptocurrency");
        orderCombobox.setItems(CryptoCurrency.values());
        orderAmountLayout.add(orderCryptoAmount, orderCombobox);
        BigDecimalField orderCryptoRate = new BigDecimalField("Rate");
        orderCryptoRate.setPlaceholder("Enter value");
        ComboBox<Order> orderType = new ComboBox<>("Type");
        orderType.setItems(Order.values());
        orderRateLayout.add(orderCryptoRate, orderType);
        Button orderBuy = new Button("Buy");
        orderBuy.addClickListener(click -> {
            if (orderCryptoAmount.isEmpty() || orderCombobox.isEmpty() || orderCryptoRate.isEmpty() || orderType.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                BigDecimal cryptoValue  = orderCryptoAmount.getValue().multiply(cryptoService.getCryptoRate(orderCombobox.getValue()));
                if (accountService.getBalance().compareTo(cryptoValue.add(cryptoService.getAllOrdersAccountValue())) < 0) {
                    sendErrorNotification("Not enough money on the account");
                } else {
                    cryptoService.addCryptoOrder(orderCryptoAmount.getValue(), orderCombobox.getValue(), orderCryptoRate.getValue(), orderType.getValue());
                    ordersGrid.setItems(cryptoService.getAllCryptoOrdersList());
                    ordersGrid.getDataProvider().refreshAll();
                    Notification notification = Notification
                            .show("Added new cryptocurrency order");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
                orderClear(orderCryptoAmount,orderCombobox, orderCryptoRate, orderType);
            }
        });

        Button orderSell = new Button("Sell");
        orderSell.addClickListener(click -> {
            if (orderCryptoAmount.isEmpty() || orderCombobox.isEmpty() || orderCryptoRate.isEmpty() || orderType.isEmpty()) {
                sendErrorNotification("Complete all fields");
            } else {
                if (cryptoService.getCryptoBalance(orderCombobox.getValue()).getBalance()
                        .compareTo(orderCryptoAmount.getValue().add(cryptoService.getAllOrdersCryptoValue(orderCombobox.getValue()))) < 0) {
                    sendErrorNotification("Not enough cryptocurrency on the account");
                } else {
                    addOrder(orderCryptoAmount,orderCombobox, orderCryptoRate, orderType, ordersGrid);
                }
                orderClear(orderCryptoAmount,orderCombobox, orderCryptoRate, orderType);
            }
        });

        Button deleteOrder = new Button("Delete order");
        deleteOrder.addClickListener(click -> {
            if (ordersGrid.getSelectionModel().getFirstSelectedItem().isEmpty()) {
                sendErrorNotification("Select order to delete");
            } else {
                cryptoService.deleteCryptoOrder(ordersGrid.getSelectionModel().getFirstSelectedItem().get().getCryptoOrderId());
                Notification notification = Notification
                        .show("Order removed");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
                ordersGrid.setItems(cryptoService.getAllCryptoOrdersList());
                ordersGrid.getDataProvider().refreshAll();
            }
        });

        orderButtonsLayout.add(orderBuy, orderSell, deleteOrder);
        exchangeLayout.add(accountBalance, accountBalanceValue, exchangeCryptoCurrency, amountLayout, buttonsLayout, orderCrypto, orderAmountLayout,
                orderRateLayout, orderButtonsLayout);

        H3 cryptoBalance = new H3("Actual balance");
        balanceGrid.setItems(cryptoService.getAllCryptoBalanceList());
        balanceGrid.setHeightByRows(true);
        balanceLayout.add(cryptoBalance, balanceGrid);

        H3 rates = new H3("Actual rates");
        //ratesGrid.setItems(cryptoService.getAllCryptoRatesList());
        ratesGrid.setHeightByRows(true);
        ratesLayout.add(rates, ratesGrid);

        H3 orders = new H3("Actual orders");
        ordersGrid.setItems(cryptoService.getAllCryptoOrdersList());
        ordersGrid.setHeightByRows(true);
        ordersLayout.add(orders, ordersGrid);

        topLayout.add(exchangeLayout, balanceLayout, ratesLayout, ordersLayout);

        H3 depositHistory = new H3("Transactions history");
        cryptoGrid.setItems(cryptoService.getAllTransactions());
        cryptoGrid.setColumns("transactionDate", "transactionAccountValue", "cryptoCurrencyCode", "transactionCryptoValue");


        cryptoLayout.add(topLayout, depositHistory, cryptoGrid);
        add(cryptoLayout);
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }

    public void exchangeClear(BigDecimalField cryptocurrencyAmount, ComboBox<CryptoCurrency> cryptocurrency) {
        cryptocurrencyAmount.clear();
        cryptocurrency.clear();
    }

    public void addOrder(BigDecimalField orderCryptoAmount, ComboBox<CryptoCurrency> orderCombobox, BigDecimalField orderCryptoRate,
                         ComboBox<Order> orderType, Grid<CryptoOrderDto> ordersGrid) {
        cryptoService.addCryptoOrder(orderCryptoAmount.getValue(), orderCombobox.getValue(), orderCryptoRate.getValue(), orderType.getValue());
        ordersGrid.setItems(cryptoService.getAllCryptoOrdersList());
        ordersGrid.getDataProvider().refreshAll();
        Notification notification = Notification
                .show("Added new cryptocurrency order");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void orderClear(BigDecimalField orderCryptoAmount, ComboBox<CryptoCurrency> orderCombobox, BigDecimalField orderCryptoRate,
                           ComboBox<Order> orderType) {
        orderCryptoAmount.clear();
        orderCombobox.clear();
        orderCryptoRate.clear();
        orderType.clear();
    }

    public void sendErrorNotification(String info) {
        Notification notification = Notification
                .show(info);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void refresh(Grid<CryptoTransactionDto> cryptoGrid, Grid<CryptoBalanceDto> balanceGrid, Grid<CryptoRatesDto> ratesGrid, Label accountBalanceValue) {
        accountBalanceValue.setText(getBalance());
        cryptoGrid.setItems(cryptoService.getAllTransactions());
        cryptoGrid.getDataProvider().refreshAll();
        balanceGrid.setItems(cryptoService.getAllCryptoBalanceList());
        balanceGrid.getDataProvider().refreshAll();
        //ratesGrid.setItems(cryptoService.getAllCryptoRatesList());
        //ratesGrid.getDataProvider().refreshAll();
    }
}
