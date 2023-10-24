package com.example.rest.controller;

import com.example.rest.dto.ProductDto;
import com.example.rest.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@SecurityRequirement(name = "apiV1")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product-barcode/{barcode}")
    public ResponseEntity<List<ProductDto>> getProductListByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productService.findProductListByBarcode(barcode));
    }

    @GetMapping("/get-product-by-name/{product_name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable(value = "product_name") String productName) {
        return ResponseEntity.ok(productService.findProductByNameLike(productName));
    }

    /**
     * Product Id si buyicha bugungi sana buyicha ostatka qaytaradi
     */
    @GetMapping("/get-product-balance-by-id/{product_id}/{warehouse_id}")
    public ResponseEntity<BigDecimal> getProductOstatokById(@PathVariable(value = "product_id") Integer productId, @PathVariable(value = "warehouse_id") Integer warehouseId) {
        BigDecimal productBalance = productService.getProductOstatokById(productId, warehouseId);
        return ResponseEntity.ok(productBalance != null ? productBalance : BigDecimal.ZERO);
    }

}


