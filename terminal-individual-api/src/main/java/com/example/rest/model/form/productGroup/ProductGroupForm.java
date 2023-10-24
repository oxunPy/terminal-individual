package com.example.rest.model.form.productGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductGroupForm {
    private Long id;

    private String name;

    private String info;

    private Long parentId;

    private String parentName;
}
