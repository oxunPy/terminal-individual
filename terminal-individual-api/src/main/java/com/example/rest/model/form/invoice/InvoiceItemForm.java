package com.example.rest.model.form.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class InvoiceItemForm {
    private Long id;

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
