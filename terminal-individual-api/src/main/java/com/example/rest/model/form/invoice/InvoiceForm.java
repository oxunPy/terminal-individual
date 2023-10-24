package com.example.rest.model.form.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class InvoiceForm {

    private Long id;

    @NotNull(message = "Идентификатор учетной записи не должен быть нулевым")
    private Long dealerClientId;

    private String printableName;

    private Long warehouseId;

    private String warehouse;

    @NotNull(message = "Дата не может быть нулевой")
    private Date date;

    private String type;

    private String info;

    private Long countOfItems;

    private BigDecimal total;
}
