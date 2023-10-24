package com.example.rest.repository;

import com.example.rest.dto.dtoProjection.ProductGroupDtoProjection;
import com.example.rest.dto.dtoProjection.ProductPriceDtoProjection;
import com.example.rest.dto.dtoProjection.UnitDtoProjection;
import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.ProductDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_product_by_barcode(:barcode)")
    List<ProductDtoProjection> getProductListByBarcode(@Param("barcode") String barcode);

    @Query(nativeQuery = true, value = "select * from get_product_by_name(:param1, :param2, :param3, :param4)")
    List<ProductDtoProjection> getProductListByNameLike(@Param("param1") String param1,
                                                        @Param("param2") String param2,
                                                        @Param("param3") String param3,
                                                        @Param("param4") String param4);

    @Query(nativeQuery = true, value = " select * from getproductostatok(:id, :warehouse_id, :date)")
    BigDecimal getProductOstatokByProductId(@Param("id") int productId, @Param("warehouse_id") int warehouseId, @Param("date") Date dateParam);

    @Query(nativeQuery = true, value = "select * from get_product_by_barcode(:barcode)", countProjection = "select count(*) from get_product_by_barcode(:barcode)")
    Page<ProductDtoProjection> pageProductsByBarcode(@Param("barcode") String barcode, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from get_product_by_name(:name, '', '', '')", countProjection = "select count(*) from get_product_by_name(:name, '', '', '')")
    Page<ProductDtoProjection> pageProductsByName(@Param("name") String productName, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from get_product_rates(:product_id, :type)", countProjection = "select count(*) from get_product_rates(:product_id, :type)")
    Page<ProductPriceDtoProjection> pageProductPrices(@Param("product_id") Long productId, @Param("type") int type, Pageable pageable);

    @Query(nativeQuery = true, value = "select last_value from product_id_seq")
    Long lastInsertProductId();


    @Query(nativeQuery = true, value = "select * from get_product_group(:name, null)", countProjection = "select count(*) from get_product_group(:name)")
    Page<ProductGroupDtoProjection> pageProductGroupByName(@Param("name") String groupName, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from get_unit()", countProjection = "select count(*) from get_unit()")
    Page<UnitDtoProjection> pageUnit(Pageable pageable);

    @Query(nativeQuery = true, value = "select create_update_unit(:name, :symbol, :id)")
    Integer createAndEditUnit(@Param("name") String name, @Param("symbol") String symbol, @Param("id") Long unitId);

    @Query(nativeQuery = true, value = "select create_update_product_group(:id, :name, :info, :parent_id)")
    Integer createAndEditProductGroup(@Param("id") Long productGroupId, @Param("name") String name, @Param("info") String info, @Param("parent_id") Long groupId);

    @Query(nativeQuery = true, value = "select delete_product_group(:id)")
    Boolean deleteProductGroup(@Param("id") Long productGroupId);

    @Query(nativeQuery = true, value = "select delete_unit(:id)")
    Boolean deleteUnit(@Param("id") Long unitId);

    @Query(nativeQuery = true, value = "select id, name, symbol from unit where status = any(ARRAY[2, 5]) and id = :id")
    UnitDtoProjection getUnitById(@Param("id") Long unitId);

    @Query(nativeQuery = true, value = "select * from get_product_group(null, :id)")
    ProductGroupDtoProjection getProductGroupById(@Param("id") Long productGroupId);
}
