package com.example.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userName;
    private String login;
    private String jwtToken;
    private String password;
}
