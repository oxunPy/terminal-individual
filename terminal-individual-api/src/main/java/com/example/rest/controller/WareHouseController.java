package com.example.rest.controller;

import com.example.rest.dto.WareHouseDto;
import com.example.rest.service.WareHouseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@SecurityRequirement(name = "apiV1")
public class WareHouseController {

    private final WareHouseService wareHouseService;

    @Autowired
    public WareHouseController(WareHouseService wareHouseService) {
        this.wareHouseService = wareHouseService;
    }

    @GetMapping
    public ResponseEntity<List<WareHouseDto>> getAllWareHouses(@RequestParam(value = "name", required = false, defaultValue = "") String warehouseName){
        return ResponseEntity.ok(wareHouseService.getAllWareHousesWithName(warehouseName));
    }


}
