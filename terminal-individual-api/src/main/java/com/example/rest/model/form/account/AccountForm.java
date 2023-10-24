package com.example.rest.model.form.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountForm {
    private Integer id;

    private String firstName;

    private String lastName;

    private String printableName;

    private String phoneNumber;

    private BigDecimal openingBalance;
}
