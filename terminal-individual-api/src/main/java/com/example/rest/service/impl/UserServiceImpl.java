package com.example.rest.service.impl;

import com.example.rest.dto.UserDto;
import com.example.rest.dto.dtoProjection.UserDtoProjection;
import com.example.rest.repository.UserRepository;
import com.example.rest.service.UserService;
import com.example.rest.utils.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto login(String login, String password) {
        String passwordEncoded = Encoder.convert(password);

        Optional<UserDtoProjection> loggedUser = userRepository.getUser(login, passwordEncoded);
        if(loggedUser.isPresent()){
            UserDto userDto = new UserDto();
            userDto.setLogin(loggedUser.get().getLogin());
            userDto.setUserName(loggedUser.get().getUser_name());
            userDto.setPassword(password);
            return userDto;
        }
        return null;
    }
}
