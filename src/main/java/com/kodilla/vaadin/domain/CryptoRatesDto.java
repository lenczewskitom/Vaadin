package com.kodilla.vaadin.domain;

import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoRatesDto {

    private long cryptoRateId;
    private BigDecimal lastRate;
    private BigDecimal rateChange;
    private CryptoCurrency cryptocurrencyCode;
}
