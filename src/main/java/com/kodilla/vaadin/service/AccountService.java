package com.kodilla.vaadin.service;

import com.kodilla.vaadin.domain.AccountBalanceDto;
import com.kodilla.vaadin.domain.AccountDepositDto;
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
    private final static String BACKEND_ENDPOINT = "https://savings-prod-kodilla-tasks-g3t498.mo4.mogenius.io/v1/account/";

    public static AccountService getInstance() {
        if (accountService == null) {
            accountService = new AccountService();
        }
        return accountService;
    }


    public List<AccountDepositDto> getAllDeposits() {
        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "deposits").build().encode().toUri();
        AccountDepositDto[] response = restTemplate.getForObject(url, AccountDepositDto[].class);
        return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
    }

    public BigDecimal getBalance() {
        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "balance").build().encode().toUri();
        return restTemplate.getForObject(url, AccountBalanceDto.class).getBalance();
    }

    public void addDeposit(BigDecimal deposit) {
        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "add")
                .queryParam("deposit", deposit).build().encode().toUri();
        restTemplate.postForObject(url,null, AccountTransactionDto.class);
    }

    public void withdrawDeposit(BigDecimal deposit) {
        URI url = UriComponentsBuilder.fromHttpUrl(BACKEND_ENDPOINT + "withdraw")
                .queryParam("deposit", deposit).build().encode().toUri();
        restTemplate.postForObject(url,null, AccountTransactionDto.class);
    }
}
