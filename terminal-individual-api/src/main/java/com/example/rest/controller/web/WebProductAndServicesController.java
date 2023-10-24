package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.file.FileForm;
import com.example.rest.model.form.product.ProductForm;
import com.example.rest.model.form.product.ProductPriceForm;
import com.example.rest.model.form.product.ProductSaveOrEditForm;
import com.example.rest.model.form.productGroup.ProductGroupForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.unit.UnitForm;
import com.example.rest.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebProductAndServicesController {

    private final ProductService productService;

    @GetMapping(ConstEndpoints.URL_AJAX_PRODUCTS_PAGE)
    @Operation(hidden = true)
    public String pageProduct(){
        return "/product/view";
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_PRODUCT_LIST)
    @Operation(hidden = true)
    public DataTablesForm<ProductForm> datatableProduct(Locale locale, @RequestBody FilterForm filterForm){
        return productService.getProductsDataTable(filterForm);
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_UPLOAD_PRODUCT_EXCEL)
    @Operation(hidden = true)
    public ResponseEntity<Object> uploadExcel(Locale locale, @ModelAttribute FileForm fileForm){
        return ResponseEntity.ok(productService.uploadExcelProducts(fileForm));
    }

    @ResponseBody
    @GetMapping(value = ConstEndpoints.URL_AJAX_DOWNLOAD_TEMPLATE_EXCEL, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(hidden = true)
    public void downloadTemplate(Locale locale, HttpServletResponse response){
        productService.getProductExcelTemplate(response);
    }

    @ResponseBody
    @GetMapping(value = ConstEndpoints.URL_AJAX_DOWNLOAD_EXCEL, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(hidden = true)
    public void downloadExcel(Locale locale, @RequestParam(value = "search", required = false, defaultValue = "") String searchText,  HttpServletResponse response) throws IOException {
        productService.getProductsExcel(locale, searchText, response);
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_PAGE_PRODUCT_GROUPS)
    @Operation(hidden = true)
    public String pageProductGroup(){
        return "/product/productGroup";
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_PAGE_UNITS)
    @Operation(hidden = true)
    public String pageUnit(){
        return "/product/unit";
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_PRODUCT_GROUP_LIST)
    @Operation(hidden = true)
    public DataTablesForm<ProductGroupForm> getProductGroupDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return productService.getProductGroupDataTable(filterForm);
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_UNIT_LIST)
    @Operation(hidden = true)
    public DataTablesForm<UnitForm> getUnitDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return productService.getUnitDataTable(filterForm);
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_PRODUCT_PRICE_LIST)
    public DataTablesForm<ProductPriceForm> getProductPriceDatatable(Locale locale, @PathVariable("id") Long id, @RequestBody FilterForm filterForm){
        return productService.getProductPriceDataTable(id, filterForm);
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_GET_UNIT)
    @Operation(hidden = true)
    public ResponseEntity<Object> getUnit(@PathVariable("id") Long unitId){
        return ResponseEntity.ok(productService.getUnit(unitId));
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_GET_PRODUCT_GROUP)
    @Operation(hidden = true)
    public ResponseEntity<Object> getProductGroup(@PathVariable("id") Long productGroupId){
        return ResponseEntity.ok(productService.getProductGroup(productGroupId));
    }

    @PutMapping(value = ConstEndpoints.URL_AJAX_EDIT_UNIT)
    @Operation(hidden = true)
    public ResponseEntity<Object> editUnit(Locale locale,
                                       @ModelAttribute UnitForm unitForm){
        return ResponseEntity.ok(productService.createAndEditUnit(unitForm));
    }
    @PostMapping(value = ConstEndpoints.URL_AJAX_SAVE_UNIT)
    @Operation(hidden = true)
    public ResponseEntity<Object> saveUnit(Locale locale,
                                       @ModelAttribute UnitForm unitForm){
        return ResponseEntity.ok(productService.createAndEditUnit(unitForm));
    }

    @PutMapping(value = ConstEndpoints.URL_AJAX_EDIT_PRODUCT_GROUP)
    @Operation(hidden = true)
    public ResponseEntity<Object> editProductGroup(Locale locale,
                                                   @ModelAttribute ProductGroupForm productGroupForm){
        return ResponseEntity.ok(productService.createAndEditProductGroup(productGroupForm));
    }
    @PostMapping(value = ConstEndpoints.URL_AJAX_SAVE_PRODUCT_GROUP)
    @Operation(hidden = true)
    public ResponseEntity<Object> saveProductGroup(Locale locale,
                                                   @ModelAttribute ProductGroupForm productGroupForm){
        return ResponseEntity.ok(productService.createAndEditProductGroup(productGroupForm));
    }

    @DeleteMapping(value = ConstEndpoints.URL_AJAX_DELETE_UNIT)
    @Operation(hidden = true)
    public ResponseEntity<Object> deleteUnit(Locale locale, @PathVariable("id") Long unitId){
        return ResponseEntity.ok(productService.deleteUnit(unitId));
    }

    @DeleteMapping(value = ConstEndpoints.URL_AJAX_DELETE_PRODUCT_GROUP)
    @Operation(hidden = true)
    public ResponseEntity<Object> deleteProductGroup(Locale locale, @PathVariable("id") Long productGroupId){
        return ResponseEntity.ok(productService.deleteProductGroup(productGroupId));
    }

    @PostMapping(value = ConstEndpoints.URL_AJAX_SAVE_OR_EDIT_PRODUCT, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(hidden = true)
    public ResponseEntity<Object> saveProduct(Locale locale, @RequestPart("body") ProductSaveOrEditForm productForm, @RequestPart("images") MultipartFile[] images){
        return ResponseEntity.ok(productService.saveProduct(productForm));
    }

    @ResponseBody
    @GetMapping(value = ConstEndpoints.URL_AJAX_GET_PRODUCT)
    @Operation(hidden = true)
    public ResponseEntity<Object> getProduct(@PathVariable("id") Long productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @DeleteMapping(value = ConstEndpoints.URL_AJAX_DELETE_PRODUCT)
    @Operation(hidden = true)
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long productId){
        return ResponseEntity.ok(false);
    }
}