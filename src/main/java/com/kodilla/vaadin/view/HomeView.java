package com.kodilla.vaadin.view;

import com.kodilla.vaadin.service.AccountService;
import com.kodilla.vaadin.service.CurrencyService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | Savings App")
public class HomeView extends VerticalLayout {

    private AccountService accountService = AccountService.getInstance();
    private CurrencyService currencyService = CurrencyService.getInstance();
    public HomeView() {
        VerticalLayout homeLayout = new VerticalLayout();
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

        homeLayout.add(allLayout, accountLayout, currenciesLayout, cryptocurrenciesLayout);

        Button addButton = new Button("Add");
        addButton.addClickListener(click -> {

        });
        addButton.addClickShortcut(Key.ENTER);

        add(homeLayout);
    }

    public String getAllSavings() {
        return accountService.getBalance() + " zł";
    }
}
