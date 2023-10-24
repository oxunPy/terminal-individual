package com.example.rest.service;

import com.example.rest.dto.WareHouseDto;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.warehouse.WareHouseForm;

import java.util.List;

public interface WareHouseService {

    List<WareHouseDto>  getAllWareHousesWithName(String name);

    DataTablesForm<WareHouseForm> getWareHouseDataTable(FilterForm filterForm);

    List<WareHouseForm> findAllWareHouse();

    Boolean deleteWarehouse(Long warehouseId);

    WareHouseForm getWarehouse(Long warehouseId);

    Boolean saveWarehouse(WareHouseForm wareHouseForm);

    Boolean editWarehouse(WareHouseForm wareHouseForm);
}
