package com.example.rest.controller.web;

import com.example.rest.constant.AccountType;
import com.example.rest.constant.ConstEndpoints;
import com.example.rest.constant.InvoiceType;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.warehouse.WareHouseForm;
import com.example.rest.service.*;
import com.example.rest.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebDataController {
    private final SettingService settingService;

    private final DealerService dealerService;

    private final ProductService productService;

    private final WareHouseService wareHouseService;

    private final AccountService accountService;

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_I18N)
    @Operation(hidden = true)
    public Map i18n(Locale locale) throws Exception{
        ResourceBundle bundle = R.bundle(locale);
        Map map = new HashMap();
        bundle.keySet().forEach(key -> map.put(key, bundle.getString(key)));
        return map;
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_COMPANY)
    @Operation(hidden = true)
    public List<Select2Form> dataCompany(){
        return settingService.findAllCompany();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_CURRENCY)
    @Operation(hidden = true)
    public List<Select2Form> dataCurrency(){
        return settingService.findAllCurrency();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_DEALER)
    @Operation(hidden = true)
    public List<Select2Form> dataDealer(){
        return dealerService.findAllDealer();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_DEALER_CLIENT)
    @Operation(hidden = true)
    public List<Select2Form> dataDealerClient(@PathVariable("account_type") AccountType accountType){
        return accountService.findAllDealerClients(accountType);
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_PRODUCT)
    @Operation(hidden = true)
    public List<Select2Form> dataProduct(){
        return productService.findAllProduct();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_PRODUCT_GROUP)
    @Operation(hidden = true)
    public List<Select2Form> dataProductGroup(){
        return productService.findAllProductGroup();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_WAREHOUSE)
    @Operation(hidden = true)
    public List<WareHouseForm> dataWarehouse(){
        return wareHouseService.findAllWareHouse();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_UNIT)
    @Operation(hidden = true)
    public List<Select2Form> dataUnit(){
        return productService.findAllUnit();
    }

    @ResponseBody
    @PostMapping(value = ConstEndpoints.URL_AJAX_DATA_INVOICE_TYPES)
    @Operation(hidden = true)
    public List<Select2Form> dataInvoiceTypes(){
        return Arrays.asList(
                            new Select2Form((long) InvoiceType.PRIXOD_BAZA.ordinal(), InvoiceType.PRIXOD_BAZA.name()),
                            new Select2Form((long) InvoiceType.VOZVRAT_BAZA.ordinal(), InvoiceType.VOZVRAT_BAZA.name()),
                            new Select2Form((long) InvoiceType.RASXOD_KLIENT.ordinal(), InvoiceType.RASXOD_KLIENT.name()),
                            new Select2Form((long) InvoiceType.VOZVRAT_KLIENT.ordinal(), InvoiceType.VOZVRAT_KLIENT.name()),
                            new Select2Form((long) InvoiceType.GODOWN.ordinal(), InvoiceType.GODOWN.name()));
    }
}
