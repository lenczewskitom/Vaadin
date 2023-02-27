package com.kodilla.vaadin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDepositDto {

    private long depositId;
    private LocalDate depositDate;
    private BigDecimal depositValue;
}
