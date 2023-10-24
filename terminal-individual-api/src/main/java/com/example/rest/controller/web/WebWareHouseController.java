package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.warehouse.WareHouseForm;
import com.example.rest.service.WareHouseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebWareHouseController {

    private final WareHouseService wareHouseService;


    @GetMapping(ConstEndpoints.URL_AJAX_WAREHOUSES_PAGE)
    @Operation(hidden = true)
    public String page(){
        return "/warehouse/view";
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_WAREHOUSES)
    @Operation(hidden = true)
    public DataTablesForm<WareHouseForm> getWarehouseDataTable(@RequestBody FilterForm filterForm){
        return wareHouseService.getWareHouseDataTable(filterForm);
    }

    @PostMapping(ConstEndpoints.URL_AJAX_WAREHOUSE_SAVE)
    @Operation(hidden = true)
    public ResponseEntity<Object> save(Locale locale,
                                       @ModelAttribute WareHouseForm wareHouseForm){
        return ResponseEntity.ok(wareHouseService.saveWarehouse(wareHouseForm));
    }

    @PutMapping(ConstEndpoints.URL_AJAX_WAREHOUSE_EDIT)
    @Operation(hidden = true)
    public ResponseEntity<Object> edit(Locale locale,
                                       @ModelAttribute WareHouseForm warehouseForm){
        return ResponseEntity.ok(wareHouseService.editWarehouse(warehouseForm));
    }

    @DeleteMapping(ConstEndpoints.URL_AJAX_WAREHOUSE_DELETE)
    @Operation(hidden = true)
    public ResponseEntity<Object> deleteWarehouse(Locale locale, @PathVariable("id") Long warehouseId){
        return ResponseEntity.ok(wareHouseService.deleteWarehouse(warehouseId));
    }

    @GetMapping(ConstEndpoints.URL_AJAX_WAREHOUSE_GET)
    @Operation(hidden = true)
    public ResponseEntity<Object> getWarehouse(@PathVariable("id") Long warehouseId){
        return ResponseEntity.ok(wareHouseService.getWarehouse(warehouseId));
    }
}
