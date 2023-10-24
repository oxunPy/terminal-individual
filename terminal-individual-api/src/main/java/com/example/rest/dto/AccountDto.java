package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String printableName;

    private String phoneNumber;

}
