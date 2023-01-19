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

        H3 exchangeCryptoCurrency = new H3("Echange Cryptocurrency");
        BigDecimalField currencyAmount = new BigDecimalField("Amount");
        ComboBox<CryptoCurrency> cryptocurrency = new ComboBox<>("Cryptocurrency");
        cryptocurrency.setItems(CryptoCurrency.values());
        amountLayout.add(currencyAmount, cryptocurrency);
        Button buy = new Button("Buy");
    }

    public String getBalance() {
        return accountService.getBalance().setScale(2, RoundingMode.CEILING) + " zł";
    }
}
