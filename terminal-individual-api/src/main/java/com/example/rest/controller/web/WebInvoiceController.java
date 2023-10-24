package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.invoice.InvoiceItemForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebInvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_INVOICE)
    @Operation(hidden = true)
    public String page(){
        return "/invoice/view";
    }

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_INVOICE_ITEM)
    @Operation(hidden = true)
    public String pageInvoiceItem(Model model, @PathVariable("id") Long invoiceId){
        model.addAttribute("invoiceId", invoiceId);
        return "/invoice/invoice_items";
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_INVOICES)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceForm> invoiceDataTable(Locale locale, @RequestBody FilterForm filterForm){
        return invoiceService.getInvoiceDatatable(locale, filterForm);
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_INVOICE_ITEMS_BY_ID)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceItemForm> invoiceItemDatatable(Locale locale, @PathVariable("id") Long invoiceId){
        return invoiceService.getInvoiceItemsDatatable(invoiceId);
    }
}