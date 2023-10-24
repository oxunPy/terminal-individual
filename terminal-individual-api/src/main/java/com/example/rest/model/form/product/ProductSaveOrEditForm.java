package com.example.rest.model.form.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSaveOrEditForm {

    private Long id;

    private String productName;

    private String barcode;

    private Long productGroupId;

    private Long baseUnitId0;

    private Long baseUnitId1;

    private Double baseUnitVal0;

    private Double baseUnitVal1;

    private ProductPriceForm buyProductPrice;

    private ProductPriceForm sellProductPrice;
}
