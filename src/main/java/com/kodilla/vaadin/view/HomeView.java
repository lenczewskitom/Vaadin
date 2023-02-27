package com.kodilla.vaadin.view;

import com.kodilla.vaadin.domain.CryptoRatesDto;
import com.kodilla.vaadin.domain.CurrencyRatesDto;
import com.kodilla.vaadin.service.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | Savings App")
public class HomeView extends VerticalLayout {

    private final AccountService accountService = AccountService.getInstance();
    private final CryptoRatesService cryptoRatesService = CryptoRatesService.getInstance();
    private final CurrencyRatesService currencyRatesService = CurrencyRatesService.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final CryptoService cryptoService = CryptoService.getInstance();
    public HomeView() {
        HorizontalLayout homeLayout = new HorizontalLayout();
        VerticalLayout savingsLayout = new VerticalLayout();
        VerticalLayout cryptoRatesLayout = new VerticalLayout();
        VerticalLayout currencyRatesLayout = new VerticalLayout();
        HorizontalLayout allLayout = new HorizontalLayout();
        HorizontalLayout accountLayout = new HorizontalLayout();
        HorizontalLayout currenciesLayout = new HorizontalLayout();
        HorizontalLayout cryptocurrenciesLayout = new HorizontalLayout();

        Label allSavings = new Label("All your savings: ");
        Label allSavingsValue = new Label(getAllSavings());
        allLayout.add(allSavings, allSavingsValue);
        Label accountSavings = new Label("Savings on account: ");
        Label accountSavingsValue = new Label(accountService.getBalance() + " zł");
        accountLayout.add(accountSavings, accountSavingsValue);
        Label currenciesSavings = new Label("Savings in currencies: ");
        Label currenciesSavingsValue = new Label("0");
        currenciesLayout.add(currenciesSavings, currenciesSavingsValue);
        Label cryptoSavings = new Label("Savings in cryptocurrencies: ");
        Label cryptoSavingsValue = new Label("0");
        cryptocurrenciesLayout.add(cryptoSavings, cryptoSavingsValue);

        savingsLayout.add(allLayout, accountLayout, currenciesLayout, cryptocurrenciesLayout);

        Label currencyRates = new Label("TOP 3 currency changes in last 24 hours:");
        Grid<CurrencyRatesDto> currencyRatesGrid = new Grid<>(CurrencyRatesDto.class, false);
        currencyRatesGrid.setItems(currencyRatesService.getTopRates());
        currencyRatesGrid.setHeightByRows(true);
        currencyRatesGrid.addColumn(CurrencyRatesDto::getCurrencyCode).setHeader("Currency");
        currencyRatesGrid.addColumn(rate -> rate.getRateChange().toString() + " %").setHeader("Rate Change");
        currencyRatesLayout.add(currencyRates);

        Label cryptoRates = new Label("TOP 3 cryptocurrency changes in last 24 hours:");
        Grid<CryptoRatesDto> cryptoRatesGrid = new Grid<>(CryptoRatesDto.class,false);
        cryptoRatesGrid.setItems(cryptoRatesService.getTopRates());
        cryptoRatesGrid.setHeightByRows(true);
        cryptoRatesGrid.addColumn(CryptoRatesDto::getCryptocurrencyCode).setHeader("Cryptocurrency");
        cryptoRatesGrid.addColumn(rate -> rate.getRateChange().toString() + " %").setHeader("Rate Change");
        cryptoRatesLayout.add(cryptoRates, cryptoRatesGrid);

        homeLayout.add(savingsLayout, cryptoRatesLayout);
        add(homeLayout);
    }

    public String getAllSavings() {
        return accountService.getBalance() + " zł";
    }
}
