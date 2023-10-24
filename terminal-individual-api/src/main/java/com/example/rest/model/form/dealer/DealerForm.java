package com.example.rest.model.form.dealer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DealerForm {
    private Long id;
    private String name;
    private String dealerCode;
}
