package com.example.rest.service.impl;

import com.example.rest.dto.DealerDto;
import com.example.rest.dto.dtoProjection.DealerDtoProjection;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.dealer.DealerForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.repository.DealerRepository;
import com.example.rest.service.DealerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
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
public class DealerServiceImpl implements DealerService {

    private final DealerRepository dealerRepository;
    @Override
    public List<DealerDto> getDealersWithName(String name) {
        List<DealerDtoProjection> ddProjections = dealerRepository.getDealersWithNameLike(name);
        List<DealerDto> dealerDtos = new ArrayList<>();
        if(ddProjections != null){
            for(DealerDtoProjection ddProjection : ddProjections){
                DealerDto dto = new DealerDto();
                BeanUtils.copyProperties(ddProjection, dto);
                dealerDtos.add(dto);
            }
        }
        return dealerDtos;
    }
    @Override
    @Transactional(readOnly = true)
    public DataTablesForm<DealerForm> getDealersDataTable(FilterForm filter) {

        Sort sort = getSort(filter.orderMap());
        if(sort.isEmpty())
            sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);
        Map<String, Object> filterMap = filter.getFilter();

        String name = null;
        if(filterMap != null){
            name = MapUtils.getString(filterMap, "name");
        }
        Page<DealerDtoProjection> pageDealers = dealerRepository.pageDealers(name, pageRequest);

        List<DealerForm> dealerList = pageDealers.stream().
                                    map(d -> new DealerForm(d.getId(), d.getName(), d.getDealerCode())).collect(Collectors.toList());

        DataTablesForm<DealerForm> dataTablesForm = new DataTablesForm<>();
        dataTablesForm.setData(dealerList);
        dataTablesForm.setDraw(filter.getDraw());
        dataTablesForm.setRecordsTotal((int) pageDealers.getTotalElements());
        dataTablesForm.setRecordsFiltered((int) pageDealers.getTotalElements());

        return dataTablesForm;
    }

    private Integer count(String name){
        return dealerRepository.count(name);
    }

    @Override
    public List<Select2Form> findAllDealer() {
        return dealerRepository.getDealersWithNameLike("").stream().map(d -> new Select2Form(d.getId(), d.getName())).collect(Collectors.toList());
    }

    private Sort getSort(Map<String, OrderForm> orderFormMap){
        List<Sort.Order> orders = new ArrayList<>();
        if(orderFormMap != null){
            orderFormMap.forEach((column, orderForm) -> {
                String property = null;
                switch(column){
                    case "name":
                        property = "name";
                        break;
                    case "dealerCode":
                        property = "dealerCode";
                }
                Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());

                orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
            });
        }
        return Sort.by(orders);
    }
}
