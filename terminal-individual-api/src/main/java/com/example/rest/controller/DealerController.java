package com.example.rest.controller;

import com.example.rest.dto.DealerDto;
import com.example.rest.model.form.dealer.DealerForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.service.DealerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dealers")
@SecurityRequirement(name = "apiV1")
@RequiredArgsConstructor
public class DealerController {
    private final DealerService dealerService;


    @GetMapping
    public ResponseEntity<List<DealerDto>> getDealers(@RequestParam("name") String name){
        return ResponseEntity.ok(dealerService.getDealersWithName(name));
    }
}
