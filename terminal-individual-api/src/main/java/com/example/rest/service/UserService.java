package com.example.rest.service;

import com.example.rest.dto.UserDto;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    UserDto login(String login, String password);
}
