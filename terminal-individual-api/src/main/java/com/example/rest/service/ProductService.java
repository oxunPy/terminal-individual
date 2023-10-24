package com.example.rest.service;

import com.example.rest.dto.ProductDto;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.file.FileForm;
import com.example.rest.model.form.product.ProductForm;
import com.example.rest.model.form.product.ProductPriceForm;
import com.example.rest.model.form.product.ProductSaveOrEditForm;
import com.example.rest.model.form.productGroup.ProductGroupForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.unit.UnitForm;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface ProductService {
    List<ProductDto> findAllProductDto();

    ProductDto saveProductDto(ProductDto dto);

    ProductDto updateProductDto(ProductDto dto);

    ProductDto findProductDtoById(Long id);

    List<ProductDto> findProductListByBarcode(String barcode);

    DataTablesForm<ProductForm> getProductsDataTable(FilterForm filterForm);

    DataTablesForm<ProductPriceForm> getProductPriceDataTable(Long productId, FilterForm filterForm);

    DataTablesForm<ProductGroupForm> getProductGroupDataTable(FilterForm filterForm);

    DataTablesForm<UnitForm> getUnitDataTable(FilterForm filterForm);

    List<ProductDto> findProductByNameLike(String productName);

    BigDecimal getProductOstatokById(Integer productId, Integer warehouseId);

    List<Select2Form> findAllProduct();

    List<Select2Form> findAllProductGroup();

    List<Select2Form> findAllUnit();

    Boolean uploadExcelProducts(FileForm fileForm);

    void getProductExcelTemplate(HttpServletResponse response);

    void getProductsExcel(Locale locale, String searchText, HttpServletResponse response);

    UnitForm getUnit(Long unitId);

    ProductGroupForm getProductGroup(Long productGroupId);

    Boolean createAndEditUnit(UnitForm unit);

    Boolean createAndEditProductGroup(ProductGroupForm productGroup);

    Boolean deleteUnit(Long unitId);

    Boolean deleteProductGroup(Long productGroupId);

    Boolean saveProduct(ProductSaveOrEditForm productSaveOrEditForm);

    ProductSaveOrEditForm getProduct(Long productId);

    Boolean deleteProduct(Long productId);
}
