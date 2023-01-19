package com.kodilla.vaadin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kodilla.vaadin.domain.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyTransactionDto {

    private long transactionId;
    private LocalDate transactionDate;
    private BigDecimal transactionAccountValue;
    private Currency currencyCode;
    private BigDecimal transactionCurrencyValue;
}
