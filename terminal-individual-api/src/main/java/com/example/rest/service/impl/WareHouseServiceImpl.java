package com.example.rest.service.impl;

import com.example.rest.dto.WareHouseDto;
import com.example.rest.dto.dtoProjection.WareHouseDtoProjection;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.model.form.warehouse.WareHouseForm;
import com.example.rest.repository.WareHouseRepository;
import com.example.rest.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;

    @Override
    public List<WareHouseDto> getAllWareHousesWithName(String name) {
        List<WareHouseDtoProjection> dtoProjections = wareHouseRepository.getAllWareHousesWithName(name);
        List<WareHouseDto> dtoList = new ArrayList<>();
        if(!dtoProjections.isEmpty()){
            for(WareHouseDtoProjection projection : dtoProjections){
                dtoList.add(WareHouseDto.buildFromDtoProjection(projection));
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesForm<WareHouseForm> getWareHouseDataTable(FilterForm filter) {
        Sort sort = getSort(filter.orderMap());

        if(sort.isEmpty()){
            sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        }

        Map<String, Object> filterMap = filter.getFilter();
        String name = null;

        if(filterMap != null){
            name = StringUtils.getIfEmpty(MapUtils.getString(filterMap, "name"), () -> "");
        }

        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<WareHouseDtoProjection> pageWareHouses = wareHouseRepository.getAllWareHousesWithName(name, 0, Integer.MAX_VALUE, pageRequest);

        List<WareHouseForm> wareHouseList = pageWareHouses.stream()
                .map(w -> new WareHouseForm(w.getId(), w.getName(), w.getIsDefault())).collect(Collectors.toList());

        DataTablesForm<WareHouseForm> dataTable = new DataTablesForm<>();
        dataTable.setData(wareHouseList);
        dataTable.setDraw(filter.getDraw());
        dataTable.setRecordsTotal((int) pageWareHouses.getTotalElements());
        dataTable.setRecordsFiltered((int) pageWareHouses.getTotalElements());
        return dataTable;
    }

    @Override
    public List<WareHouseForm> findAllWareHouse() {
        return wareHouseRepository.getAllWareHousesWithName("").stream()
                .map(w -> new WareHouseForm(w.getId(), w.getName(), w.getIsDefault())).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteWarehouse(Long warehouseId) {
        return wareHouseRepository.deleteWareHouse(warehouseId);
    }

    @Override
    public WareHouseForm getWarehouse(Long warehouseId) {
        return getAllWareHousesWithName("").stream().filter(w -> Objects.equals(w.getId(), warehouseId)).map(w -> new WareHouseForm(w.getId(), w.getName(), w.getIsDefault())).findFirst().orElse(null);
    }

    @Override
    public Boolean saveWarehouse(WareHouseForm wareHouseForm) {
        return wareHouseRepository.saveWareHouse(wareHouseForm.getName(), wareHouseForm.getIsDefault() != null);
    }

    @Override
    public Boolean editWarehouse(WareHouseForm warehouseForm){
        if(warehouseForm.getId() == null) return false;
        return wareHouseRepository.editWareHouse(warehouseForm.getName(), warehouseForm.getIsDefault() != null, warehouseForm.getId());
    }

    public Sort getSort(Map<String, OrderForm> orderMap){
        List<Sort.Order> orders = new ArrayList<>();

        orderMap.forEach((column, orderForm) -> {
            String property = null;

            switch(column){
                case "name":
                    property = "name";
                    break;
                case "isDefault":
                    property = "isDefault";
                    break;
            }
            Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());
            orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
        });
        return Sort.by(orders);
    }

}