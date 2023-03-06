package com.kodilla.vaadin.domain;

import com.kodilla.vaadin.domain.enums.DepositType;
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
    private DepositType depositType;
}
