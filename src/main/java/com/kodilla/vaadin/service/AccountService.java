package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.AccountBalanceDto;
import com.kodilla.vaadin.domain.AccountTransactionDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AccountService {

    private static AccountService accountService;
    private final RestTemplate restTemplate = new RestTemplate();

    public static AccountService getInstance() {
        if (accountService == null) {
            accountService = new AccountService();
        }
        return accountService;
    }


    public List<AccountTransactionDto> getAllDeposits() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/account/deposits").build().encode().toUri();
        AccountTransactionDto[] response = restTemplate.getForObject(url, AccountTransactionDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getBalance() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/account/balance").build().encode().toUri();
        return restTemplate.getForObject(url, AccountBalanceDto.class).getBalance();
    }

    public void addDeposit(BigDecimal deposit) {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/account")
                .queryParam("deposit", deposit).build().encode().toUri();
        restTemplate.postForObject(url,null, AccountTransactionDto.class);
    }
}
