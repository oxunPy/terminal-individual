package com.example.rest.service;

import com.example.rest.constant.AccountType;
import com.example.rest.dto.AccountDto;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.account.AccountForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAllAccountsWithType(int type);

    List<AccountDto> getAllAccountsWithTypeAndNameLike(int type, String name);

    List<Select2Form> findAllDealerClients(AccountType accountType);

    DataTablesForm<AccountForm> getAccountDataTable(FilterForm filterForm);
}
