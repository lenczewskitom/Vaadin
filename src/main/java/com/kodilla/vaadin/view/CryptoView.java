package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.CryptoTransactionDto;
import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import com.kodilla.vaadin.domain.enums.Currency;
import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CryptoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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

        H3 accountBalance = new H3("Actual account balance");
        Label accountBalanceValue = new Label(getBalance());

        H3 exchangeCryptoCurrency = new H3("Exchange Cryptocurrency");
        BigDecimalField cryptocurrencyAmount = new BigDecimalField("Amount");
        ComboBox<CryptoCurrency> cryptocurrency = new ComboBox<>("Cryptocurrency");
        cryptocurrency.setItems(CryptoCurrency.values());
        amountLayout.add(cryptocurrencyAmount, cryptocurrency);
        Button buy = new Button("Buy");
        Button sell = new Button("Sell");

        buttonsLayout.add(buy, sell);
        exchangeLayout.add(accountBalance, accountBalanceValue, exchangeCryptoCurrency, amountLayout, buttonsLayout);

        H3 currencyBalance = new H3("Currencies balance");
        Label btc = new Label(CryptoCurrency.BTC + " - " + cryptoService.getCryptoBalance(CryptoCurrency.BTC).getBalance().setScale(2, RoundingMode.CEILING));
        Label eth = new Label(CryptoCurrency.ETH + " - " + cryptoService.getCryptoBalance(CryptoCurrency.ETH).getBalance().setScale(2, RoundingMode.CEILING));
        Label ltc = new Label(CryptoCurrency.LTC + " - " + cryptoService.getCryptoBalance(CryptoCurrency.LTC).getBalance().setScale(2, RoundingMode.CEILING));
        Label sol = new Label(CryptoCurrency.SOL + " - " + cryptoService.getCryptoBalance(CryptoCurrency.SOL).getBalance().setScale(2, RoundingMode.CEILING));
        Label doge = new Label(CryptoCurrency.DOGE + " - " + cryptoService.getCryptoBalance(CryptoCurrency.DOGE).getBalance().setScale(2, RoundingMode.CEILING));
        balanceLayout.add(currencyBalance, btc, eth, ltc, sol, doge);

        H3 rates = new H3("Actual rates");
        Label bitcoin = new Label(CryptoCurrency.BTC + " - " + cryptoService.getCryptoRate(CryptoCurrency.BTC));
        Label ethereum = new Label(CryptoCurrency.ETH + " - " + cryptoService.getCryptoRate(CryptoCurrency.ETH));
        Label litecoin = new Label(CryptoCurrency.LTC + " - " + cryptoService.getCryptoRate(CryptoCurrency.LTC));
        Label solana = new Label(CryptoCurrency.SOL + " - " + cryptoService.getCryptoRate(CryptoCurrency.SOL));
        Label dogecoin = new Label(CryptoCurrency.DOGE + " - " + cryptoService.getCryptoRate(CryptoCurrency.DOGE));

        ratesLayout.add(rates, bitcoin, ethereum, litecoin, solana, dogecoin);
        topLayout.add(exchangeLayout, balanceLayout, ratesLayout);

        H3 depositHistory = new H3("Transactions history");
        cryptoGrid.setItems(cryptoService.getAllTransactions());
        cryptoGrid.setColumns("transactionId", "transactionDate", "transactionAccountValue", "cryptoCurrencyCode", "transactionCryptoValue");


        cryptoLayout.add(topLayout, depositHistory, cryptoGrid);
        add(cryptoLayout);
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }
}
