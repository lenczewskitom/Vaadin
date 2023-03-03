package com.kodilla.vaadin.domain;

import com.kodilla.vaadin.domain.enums.CryptoCurrency;
import com.kodilla.vaadin.domain.enums.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoOrderDto {

    private long cryptoOrderId;
    private LocalDate cryptoOrderDate;
    private BigDecimal orderCryptoValue;
    private CryptoCurrency cryptoCode;
    private BigDecimal cryptoRate;
    private Order operationType;
}
