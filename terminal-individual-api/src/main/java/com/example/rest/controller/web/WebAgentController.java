package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.account.AccountForm;
import com.example.rest.model.form.dealer.DealerForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.service.AccountService;
import com.example.rest.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebAgentController {

    private final DealerService dealerService;

    private final AccountService accountService;

    @GetMapping(value = ConstEndpoints.URL_AJAX_DEALERS_PAGE)
    @Operation(hidden = true)
    public String pageDealers(){
        return "/dealer/view";
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DEALERS_ALL)
    @Operation(hidden = true)
    public DataTablesForm<DealerForm> list(@RequestBody FilterForm filterForm) throws Exception{
        return dealerService.getDealersDataTable(filterForm);
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_PAGE_CLIENT)
    @Operation(hidden = true)
    public String pageClients(){
        return "/agent/clients";
    }

    @GetMapping(value = ConstEndpoints.URL_AJAX_PAGE_SUPPLIER)
    @Operation(hidden = true)
    public String pageSuppliers(){
        return "/agent/suppliers";
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_ACCOUNTS)
    @Operation(hidden = true)
    public DataTablesForm<AccountForm> getAccountDataTable(@RequestBody FilterForm filterForm){
        return accountService.getAccountDataTable(filterForm);
    }
}
