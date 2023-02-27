package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.CryptoTransactionDto;
import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CryptoService;
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
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Route(value = "crypto", layout = MainLayout.class)
@PageTitle("Crypto | Savings App")
public class CryptoView extends HorizontalLayout {

    private AccountService accountService= AccountService.getInstance();
    private CryptoService cryptoService = CryptoService.getInstance();

    public CryptoView() {
        VerticalLayout cryptoLayout = new VerticalLayout();
        VerticalLayout exchangeLayout = new VerticalLayout();
        VerticalLayout balanceLayout = new VerticalLayout();
        VerticalLayout ratesLayout = new VerticalLayout();
        HorizontalLayout topLayout = new HorizontalLayout();
        HorizontalLayout amountLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Grid<CryptoTransactionDto> cryptoGrid = new Grid<>(CryptoTransactionDto.class);
        cryptoGrid.setItems(cryptoService.getAllTransactions());

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 exchangeCryptoCurrency = new H3("Exchange Cryptocurrency");
        BigDecimalField cryptocurrencyAmount = new BigDecimalField("Amount");
        ComboBox<CryptoCurrency> cryptocurrency = new ComboBox<>("Cryptocurrency");
        cryptocurrency.setItems(CryptoCurrency.values());
        amountLayout.add(cryptocurrencyAmount, cryptocurrency);
        Button buy = new Button("Buy");
        buy.addClickListener(click -> {
            BigDecimal accountValue = cryptocurrencyAmount.getValue().multiply(cryptoService.getCryptoRate(cryptocurrency.getValue()));
            if (accountService.getBalance().compareTo(accountValue) < 0) {
                Notification notification = Notification
                        .show("Not enough money on the account");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            } else {
                cryptoService.buyCryptocurrency(accountValue, cryptocurrency.getValue(), cryptocurrencyAmount.getValue());
                accountBalanceValue.setText(getBalance());
                cryptoGrid.setItems(cryptoService.getAllTransactions());
                cryptoGrid.getDataProvider().refreshAll();
                Notification notification = Notification
                        .show(cryptocurrencyAmount.getValue() + " " + cryptocurrency.getValue() + " added to the account");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }
        });

        Button sell = new Button("Sell");

        buttonsLayout.add(buy, sell);
        exchangeLayout.add(accountBalance, accountBalanceValue, exchangeCryptoCurrency, amountLayout, buttonsLayout);

        H3 currencyBalance = new H3("Currencies balance");
        Label btc = new Label(CryptoCurrency.BTC + " - " + cryptoService.getCryptoBalance(CryptoCurrency.BTC).getBalance().setScale(2, RoundingMode.CEILING));
        Label eth = new Label(CryptoCurrency.ETC + " - " + cryptoService.getCryptoBalance(CryptoCurrency.ETC).getBalance().setScale(2, RoundingMode.CEILING));
        Label ltc = new Label(CryptoCurrency.LTC + " - " + cryptoService.getCryptoBalance(CryptoCurrency.LTC).getBalance().setScale(2, RoundingMode.CEILING));
        Label sol = new Label(CryptoCurrency.SOL + " - " + cryptoService.getCryptoBalance(CryptoCurrency.SOL).getBalance().setScale(2, RoundingMode.CEILING));
        Label doge = new Label(CryptoCurrency.DOGE + " - " + cryptoService.getCryptoBalance(CryptoCurrency.DOGE).getBalance().setScale(2, RoundingMode.CEILING));
        balanceLayout.add(currencyBalance, btc, eth, ltc, sol, doge);

//        H3 rates = new H3("Actual rates");
//        Label bitcoin = new Label(CryptoCurrency.BTC + " - " + cryptoService.getCryptoRate(CryptoCurrency.BTC).setScale(2, RoundingMode.CEILING));
//        Label ethereum = new Label(CryptoCurrency.ETC + " - " + cryptoService.getCryptoRate(CryptoCurrency.ETC).setScale(2, RoundingMode.CEILING));
//        Label litecoin = new Label(CryptoCurrency.LTC + " - " + cryptoService.getCryptoRate(CryptoCurrency.LTC).setScale(2, RoundingMode.CEILING));
//        Label solana = new Label(CryptoCurrency.SOL + " - " + cryptoService.getCryptoRate(CryptoCurrency.SOL).setScale(2, RoundingMode.CEILING));
//        Label dogecoin = new Label(CryptoCurrency.DOGE + " - " + cryptoService.getCryptoRate(CryptoCurrency.DOGE).setScale(2, RoundingMode.CEILING));

//        ratesLayout.add(rates, bitcoin, ethereum, litecoin, solana, dogecoin);
        topLayout.add(exchangeLayout, balanceLayout, ratesLayout);

        H3 depositHistory = new H3("Transactions history");
        cryptoGrid.setItems(cryptoService.getAllTransactions());
        cryptoGrid.setColumns("transactionDate", "transactionAccountValue", "cryptoCurrencyCode", "transactionCryptoValue");


        cryptoLayout.add(topLayout, depositHistory, cryptoGrid);
        add(cryptoLayout);
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }
}
