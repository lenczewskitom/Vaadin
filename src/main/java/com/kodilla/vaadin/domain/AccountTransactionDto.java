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
public class AccountTransactionDto {

    private long depositId;
    private LocalDate depositDate;
    private BigDecimal depositValue;
    private Currency currencyCode;
}
