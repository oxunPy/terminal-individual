package com.example.rest.controller;

import com.example.rest.dto.AccountDto;
import com.example.rest.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@SecurityRequirement(name = "apiV1")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * @param type - account type [BANK (0), CASH(1), CREDITOR(2), DEBITOR(3), EXPENSE(4)]
     * @return List of Accounts
     */

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccountWithType(@RequestParam int type) {
        return ResponseEntity.ok(accountService.getAllAccountsWithType(type));
    }
    @GetMapping("/name")
    public ResponseEntity<List<AccountDto>> getAllAccountsWithTypeAndNameLike(@RequestParam(name = "type") int type,
                                                                              @RequestParam(name = "name") String name){
        return ResponseEntity.ok(accountService.getAllAccountsWithTypeAndNameLike(type, name));
    }
}
