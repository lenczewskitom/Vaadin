package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.CryptoRatesDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CryptoRatesService {

    private static CryptoRatesService cryptoRatesService;

    private final RestTemplate restTemplate = new RestTemplate();

    public static CryptoRatesService getInstance() {
        if (cryptoRatesService == null) {
            cryptoRatesService = new CryptoRatesService();
        }
        return cryptoRatesService;
    }

    private final static String BACKEND_ENDPOINT = "https://savings-prod-kodilla-tasks-g3t498.mo4.mogenius.io/v1/cryptoRates/";

    public List<CryptoRatesDto> getTopRates() {
        URI uri = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "top").build().encode().toUri();
        CryptoRatesDto[] response = restTemplate.getForObject(uri, CryptoRatesDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }
}
