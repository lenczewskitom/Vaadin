package com.kodilla.vaadin.domain;

import com.kodilla.vaadin.domain.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesDto {

    private long currencyRateId;
    private BigDecimal lastRate;
    private BigDecimal rateChange;
    private Currency currencyCode;

}
