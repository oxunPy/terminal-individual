package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {

    private Long id;

    private Long invoiceId;

    private Long productId;

    private String productName;

    @DecimalMin(value = "0", message = "Значение количества должно быть положительным")
    private BigDecimal quantity;

    @DecimalMin(value = "0", message = "Значение курса должно быть положительным")
    private BigDecimal rate; // sotishi kerak bo'lgan bahosi (UZS)

    @DecimalMin(value = "0", message = "Значение курса продажи должно быть положительным")
    private BigDecimal sellingRate; // sotgan bahosi (UZS)

    @DecimalMin(value = "0", message = "Значение первоначальной продажи быть положительным")
    private BigDecimal originalRate; // sotish bahosi (UZS/USD)

    @NotNull(message = "Значение валюты не должно быть нулевым")
    private String currencyCode; // sotish bahosidagi valyuta (UZS/USD)

    private BigDecimal total;

}
