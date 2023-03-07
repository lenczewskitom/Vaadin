package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.CryptoRatesDto;
import com.kodilla.vaadin.domain.CurrencyRatesDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrencyRatesService {

    private static CurrencyRatesService currencyRatesService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final static String BACKEND_ENDPOINT = "https://savings-prod-kodilla-tasks-g3t498.mo4.mogenius.io/v1/currencyRates/";

    public static CurrencyRatesService getInstance() {
        if (currencyRatesService == null) {
            currencyRatesService = new CurrencyRatesService();
        }
        return currencyRatesService;
    }

    public List<CurrencyRatesDto> getTopRates() {

        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "top").build().encode().toUri();
        CurrencyRatesDto[] response = restTemplate.getForObject(uri, CurrencyRatesDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

}
