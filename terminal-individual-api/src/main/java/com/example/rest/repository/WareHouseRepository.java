package com.example.rest.repository;

import com.example.rest.dto.dtoProjection.WareHouseDtoProjection;
import com.example.rest.entity.RootEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareHouseRepository extends JpaRepository<RootEntity, Long> {


    @Query(nativeQuery = true, value = "select * from get_warehouses_by_name(:name, 0, 10000000)")
    List<WareHouseDtoProjection> getAllWareHousesWithName(@Param("name") String name);

    @Query(nativeQuery = true, value = "select * from get_warehouses_by_name(:nameLike, :offset, :limit)",
                               countProjection = "select count(*) from get_warehouses_by_name(:nameLike, :offset, :limit)")
    Page<WareHouseDtoProjection> getAllWareHousesWithName(@Param("nameLike") String name, @Param("offset") int start, @Param("limit") int count, Pageable pageable);

    @Query(nativeQuery = true, value = "select delete_warehouse(:warehouse_id)")
    Boolean deleteWareHouse(@Param("warehouse_id") Long warehouseId);

    @Query(nativeQuery = true, value = "select save_warehouse(:name, :is_default)")
    Boolean saveWareHouse(@Param("name") String name, @Param("is_default") Boolean isDefault);

    @Query(nativeQuery = true, value = "select edit_warehouse(:name, :is_default, :warehouse_id)")
    Boolean editWareHouse(@Param("name") String name, @Param("is_default") boolean isDefault, @Param("warehouse_id") Long warehouseId);
}
