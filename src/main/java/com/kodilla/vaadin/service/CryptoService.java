package com.kodilla.vaadin.service;

import org.springframework.web.client.RestTemplate;

public class CryptoService {

    private static CryptoService cryptoService;
    private final RestTemplate restTemplate = new RestTemplate();

    public static CryptoService getInstance() {
        if (cryptoService == null) {
            cryptoService = new CryptoService();
        }
        return cryptoService;
    }


}
