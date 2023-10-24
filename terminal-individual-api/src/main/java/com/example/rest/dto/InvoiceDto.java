package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;

    @NotNull(message = "Идентификатор учетной записи не должен быть нулевым")
    private Long accountId;

    private String accountPrintableName;

    @NotNull(message = "Дата не может быть нулевой")
    private String date;

    @NotNull(message = "Тип счета-фактуры не может быть нулевой")
    @Min(value = 0, message = "Минимальное значение типов счетов должно быть больше 0")
    @Max(value = 7, message = "Максимальное значение типов счетов должно быть меньше 7")
    private int type;

    private String info;

    private Integer status;

    private Long countOfItems;

    private BigDecimal total;

    @NotNull(message = "Cписок элементов счета-фактуры не должен быть нулевым")
    @NotEmpty(message = "Cписок элементов счета-фактуры не должен быть пустым")
    private List<InvoiceItemDto> invoiceItemDtoList;
}
