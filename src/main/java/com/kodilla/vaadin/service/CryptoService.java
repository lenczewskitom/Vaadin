package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.*;
import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CryptoService {

    private static CryptoService cryptoService;
    private final RestTemplate restTemplate = new RestTemplate();

    public static CryptoService getInstance() {
        if (cryptoService == null) {
            cryptoService = new CryptoService();
        }
        return cryptoService;
    }

    public BigDecimal getCryptoRate(CryptoCurrency cryptoCurrencyCode) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/cryptocurrency/rate/" + cryptoCurrencyCode)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CryptoRatesDto.class).getRate();
    }

    public List<CryptoTransactionDto> getAllTransactions() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/cryptocurrency/transactions").build().encode().toUri();
        CryptoTransactionDto[] response = restTemplate.getForObject(uri, CryptoTransactionDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public CryptoBalanceDto getCryptoBalance(CryptoCurrency cryptoCurrency) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/cryptocurrency/balance/" + cryptoCurrency)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CryptoBalanceDto.class);
    }

    public void buyCryptocurrency(BigDecimal accountValue, CryptoCurrency cryptocurrencyCode, BigDecimal cryptocurrencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/cryptocurrency/buy")
                .queryParam("accountValue", accountValue)
                .queryParam("cryptoCurrencyCode", cryptocurrencyCode)
                .queryParam("cryptocurrencyValue", cryptocurrencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CryptoTransactionDto.class);
    }

    public void sellCurrency(BigDecimal accountValue, CryptoCurrency cryptocurrencyCode, BigDecimal cryptocurrencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/cryptocurrency/sell")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", cryptocurrencyCode)
                .queryParam("currencyValue", cryptocurrencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }
}
