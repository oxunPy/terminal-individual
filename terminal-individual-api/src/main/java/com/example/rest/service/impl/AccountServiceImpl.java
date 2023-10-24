package com.example.rest.service.impl;

import com.example.rest.constant.AccountType;
import com.example.rest.constant.Status;
import com.example.rest.dto.AccountDto;
import com.example.rest.dto.dtoProjection.AccountDtoProjection;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.account.AccountForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.repository.AccountRepository;
import com.example.rest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    
    @Override
    public List<AccountDto> getAllAccountsWithType(int type) {
        List<AccountDtoProjection> accountDtoProjectionList = 
                accountRepository.getAllAccountsWithType(type, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal());

        return accountDtoProjectionList.stream()
                .map(accountDtoProjection -> new AccountDto(accountDtoProjection.getId(),
                        accountDtoProjection.getFirst_name(),
                        accountDtoProjection.getLast_name(),
                        accountDtoProjection.getPrintable_name(),
                        accountDtoProjection.getPhone()))
                .collect(Collectors.toList());
    }
    @Override
    public List<AccountDto> getAllAccountsWithTypeAndNameLike(int type, String name) {
        List<AccountDtoProjection> accountDtoProjectionList =
                accountRepository.getAllAccountsWithTypeAndNameLike(type, name, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal());

        return accountDtoProjectionList.stream()
                .map(accountDtoProjection -> new AccountDto(accountDtoProjection.getId(),
                        accountDtoProjection.getFirst_name(),
                        accountDtoProjection.getLast_name(),
                        accountDtoProjection.getPrintable_name(),
                        accountDtoProjection.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Select2Form> findAllDealerClients(AccountType accountType){
        List<AccountDtoProjection> accountDtoProjectionList = accountRepository.getAllAccountsWithType(accountType.ordinal(), Status.ACTIVE.ordinal(), Status.UPDATED.ordinal());
        return accountDtoProjectionList.stream().map(a -> new Select2Form(a.getId().longValue(), a.getPrintable_name())).collect(Collectors.toList());
    }

    @Override
    public DataTablesForm<AccountForm> getAccountDataTable(FilterForm filter){
        Sort sort = getSort(filter.orderMap());

        if(sort.isEmpty()){
            sort = Sort.by(Sort.Order.desc( "id"));
        }

        String accountType = null;
        Map<String, Object> filterMap = filter.getFilter();
        if(filterMap != null){
            accountType = MapUtils.getString(filterMap, "accountType");
        }
        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<AccountDtoProjection> pageAccounts = accountRepository.pageAllAccountsWithType(AccountType.valueOf(accountType).ordinal(), Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), pageRequest);
        List<AccountForm> accountFormList = pageAccounts.stream().map(a -> new AccountForm(a.getId(), a.getFirst_name(), a.getLast_name(), a.getPrintable_name(), a.getPhone(), a.getOpening_balance())).collect(Collectors.toList());

        DataTablesForm<AccountForm> datatable = new DataTablesForm<>();
        datatable.setRecordsTotal((int) pageAccounts.getTotalElements());
        datatable.setRecordsFiltered((int) pageAccounts.getTotalElements());
        datatable.setData(accountFormList);
        return datatable;
    }

    private Sort getSort(Map<String, OrderForm> orderMap){
        List<Sort.Order> orders = new ArrayList<>();
        if(orderMap != null){
            orderMap.forEach((column, orderForm) -> {
                String property = null;
                switch(column){
                    case "printableName":
                        property = "printable_name";
                    case "firstName":
                        property = "first_name";
                }
                Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());
                orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
            });
        }
        return Sort.by(orders);
    }
}
