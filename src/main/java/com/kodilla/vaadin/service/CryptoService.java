package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.*;
import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import com.kodilla.vaadin.domain.enums.Order;
import org.springframework.beans.factory.annotation.Value;
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

    private final String BACKEND_ENDPOINT = "http://localhost:8080/v1/";

    public BigDecimal getCryptoRate(CryptoCurrency cryptoCurrencyCode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/rate/" + cryptoCurrencyCode)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CoinApiResponseDto.class).getRate();
    }

    public List<CryptoTransactionDto> getAllTransactions() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/transactions").build().encode().toUri();
        CryptoTransactionDto[] response = restTemplate.getForObject(uri, CryptoTransactionDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public CryptoBalanceDto getCryptoBalance(CryptoCurrency cryptoCurrency) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/balance/" + cryptoCurrency)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, CryptoBalanceDto.class);
    }

    public List<CryptoBalanceDto> getAllCryptoBalanceList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/balanceList")
                .build().encode().toUri();
        CryptoBalanceDto[] response = restTemplate.getForObject(uri, CryptoBalanceDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public List<CryptoRatesDto> getAllCryptoRatesList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoRates/ratesList")
                .build().encode().toUri();
        CryptoRatesDto[] response = restTemplate.getForObject(uri, CryptoRatesDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getAllSavings() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/all").build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public void buyCryptocurrency(BigDecimal accountValue, CryptoCurrency cryptocurrencyCode, BigDecimal cryptocurrencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/buy")
                .queryParam("accountValue", accountValue)
                .queryParam("cryptoCurrencyCode", cryptocurrencyCode)
                .queryParam("cryptocurrencyValue", cryptocurrencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CryptoTransactionDto.class);
    }

    public void sellCurrency(BigDecimal accountValue, CryptoCurrency cryptocurrencyCode, BigDecimal cryptocurrencyValue) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptocurrency/sell")
                .queryParam("accountValue", accountValue)
                .queryParam("currencyCode", cryptocurrencyCode)
                .queryParam("currencyValue", cryptocurrencyValue).build().encode().toUri();
        restTemplate.postForObject(uri, null, CurrencyTransactionDto.class);
    }

    public void addCryptoOrder(BigDecimal cryptoValue, CryptoCurrency cryptoCode,
                                 BigDecimal cryptoRate, Order operationType) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoOrder")
                .queryParam("cryptoValue", cryptoValue)
                .queryParam("cryptoCode", cryptoCode)
                .queryParam("cryptoRate", cryptoRate)
                .queryParam("operationType", operationType).build().encode().toUri();
        restTemplate.postForObject(uri, null, CryptoOrderDto.class);
    }

    public List<CryptoOrderDto> getAllCryptoOrdersList() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoOrder")
                .build().encode().toUri();
        CryptoOrderDto[] response = restTemplate.getForObject(uri, CryptoOrderDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getAllOrdersAccountValue() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoOrder/ordersValue")
                .build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public BigDecimal getAllOrdersCryptoValue(CryptoCurrency cryptoCode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoOrder/cryptoValue/" + cryptoCode)
                .build().encode().toUri();
        return restTemplate.getForObject(uri, BigDecimal.class);
    }

    public void deleteCryptoOrder(Long id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "cryptoOrder/" + id)
                .build().encode().toUri();
        restTemplate.delete(uri);
    }
}
