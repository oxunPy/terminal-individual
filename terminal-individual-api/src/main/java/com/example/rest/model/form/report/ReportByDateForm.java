package com.example.rest.model.form.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportByDateForm {

    private Integer invoiceItemId;

    private String dateOfSell;

    private BigDecimal productQuantity;

    private BigDecimal productPrice;

    private Integer typeOfOperation;

    private String productName;

    private Integer priceCurrency;
}
