package com.example.rest.model.form.unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UnitForm {
    private Long id;

    private String name;

    private String symbol;
}
