package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.*;
import com.kodilla.vaadin.domain.enums.Currency;
import com.kodilla.vaadin.domain.enums.Order;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private static CurrencyService currencyService;
    private final RestTemplate restTemplate = new RestTemplate();

    public static CurrencyService getInstance() {
        if (currencyService == null) {
            currencyService = new CurrencyService();
        }
        return currencyService;
    }
    private final static String BACKEND_ENDPOINT = "https://savings-prod-kodilla-tasks-g3t498.mo4.mogenius.io/v1/";


    public BigDecimal getExchangeRate(Currency currency) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/rate")
                .queryParam("currency", currency).build().encode().toUri();
        return restTemplate.getForObject(uri, RatesDto.class).getRate();
    }

    public List<CurrencyTransactionDto> getAllTransactions() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/transactions").build().encode().toUri();
        CurrencyTransactionDto[] response = restTemplate.getForObject(uri, CurrencyTransactionDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getAllSavings() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/all").build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public CurrencyBalanceDto getCurrencyBalance(Currency currency) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/balance/" + currency)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CurrencyBalanceDto.class);
    }

    public List<CurrencyBalanceDto> getAllCurrencyBalanceList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/balanceList")
                .build().encode().toUri();
        CurrencyBalanceDto[] response = restTemplate.getForObject(uri, CurrencyBalanceDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public List<CurrencyRatesDto> getAllCurrencyRatesList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyRates/ratesList")
                .build().encode().toUri();
        CurrencyRatesDto[] response = restTemplate.getForObject(uri, CurrencyRatesDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public void buyCurrency(BigDecimal accountValue, Currency currencyCode, BigDecimal currencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/buy")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", currencyCode)
                .queryParam("currencyValue", currencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }

    public void sellCurrency(BigDecimal accountValue, Currency currencyCode, BigDecimal currencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currency/sell")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", currencyCode)
                .queryParam("currencyValue", currencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }

    public void addCurrencyOrder(BigDecimal currencyValue, Currency currencyCode,
                                 BigDecimal currencyRate, Order operationType) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyOrder/addOrder")
                .queryParam("currencyValue", currencyValue)
                .queryParam("currencyCode", currencyCode)
                .queryParam("currencyRate", currencyRate)
                .queryParam("operationType", operationType).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyOrderDto.class);
    }

    public List<CurrencyOrderDto> getAllCurrencyOrdersList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyOrder/allOrders")
                .build().encode().toUri();
        CurrencyOrderDto[] response = restTemplate.getForObject(uri, CurrencyOrderDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getAllOrdersAccountValue() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyOrder/ordersValue")
                .build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public BigDecimal getAllOrdersCurrencyValue(Currency currencyCode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyOrder/currencyValue/" + currencyCode)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public void deleteCurrencyOrder(Long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "currencyOrder/" + id)
                .build().encode().toUri();
                restTemplate.delete(uri);
    }
}
