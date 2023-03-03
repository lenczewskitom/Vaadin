package com.kodilla.vaadin.domain;

import com.kodilla.vaadin.domain.enums.Currency;
import com.kodilla.vaadin.domain.enums.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyOrderDto {

    private long currencyOrderId;
    private LocalDate currencyOrderDate;
    private BigDecimal orderCurrencyValue;
    private Currency currencyCode;
    private BigDecimal currencyRate;
    private Order operationType;
}
