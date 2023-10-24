package com.example.rest.controller;

import com.example.rest.config.JwtTokenUtil;
import com.example.rest.dto.UserDto;
import com.example.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login/{login}/{password}")
    public ResponseEntity<UserDto> getUserLoginPass(@PathVariable(value = "login") String login,
                                                    @PathVariable(value = "password") String password,
                                                    Model model){
        UserDto dto = userService.login(login, password);
        if(dto == null) return ResponseEntity.notFound().build();
        dto.setJwtToken(jwtTokenUtil.generateToken(dto.getLogin()));
        model.addAttribute("name", "Saidov Oxunjon");
        return ResponseEntity.ok(dto);
    }
}
