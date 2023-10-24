package com.example.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReportByDateDto {

    private Integer invoiceItemId;

    private LocalDate dateOfSell;

    private BigDecimal productQuantity;

    private BigDecimal productPrice;

    private Integer typeOfOperation;

    private String productName;

    private Integer priceCurrency;

}
