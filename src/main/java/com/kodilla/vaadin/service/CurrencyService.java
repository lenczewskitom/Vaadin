package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.CurrencyBalanceDto;
import com.kodilla.vaadin.domain.CurrencyTransactionDto;
import com.kodilla.vaadin.domain.RatesDto;
import com.kodilla.vaadin.domain.enums.Currency;
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

    public BigDecimal getExchangeRate(Currency currency) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/currency/rate")
                .queryParam("currency", currency).build().encode().toUri();
        return restTemplate.getForObject(uri, RatesDto.class).getRate();
    }

    public List<CurrencyTransactionDto> getAllTransactions() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/currency/transactions").build().encode().toUri();
        CurrencyTransactionDto[] response = restTemplate.getForObject(uri, CurrencyTransactionDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public CurrencyBalanceDto getCurrencyBalance(Currency currency) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/currency/balance/" + currency)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CurrencyBalanceDto.class);
    }

    public void buyCurrency(BigDecimal accountValue, Currency currencyCode, BigDecimal currencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/currency/buy")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", currencyCode)
                .queryParam("currencyValue", currencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }

    public void sellCurrency(BigDecimal accountValue, Currency currencyCode, BigDecimal currencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/currency/sell")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", currencyCode)
                .queryParam("currencyValue", currencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }
}
