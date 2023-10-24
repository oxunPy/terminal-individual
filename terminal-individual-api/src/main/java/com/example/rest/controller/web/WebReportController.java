package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.report.ReportByDateForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebReportController {

    private final ReportService reportService;

    @GetMapping(ConstEndpoints.URL_AJAX_REPORTS_PAGE)
    @Operation(hidden = true)
    public String page(){
        return "/report/view";
    }


    @PostMapping(ConstEndpoints.URL_AJAX_REPORTS_BY_DATE)
    @Operation(hidden = true)
    public DataTablesForm<ReportByDateForm> getDataTableReports(Locale locale, @RequestBody FilterForm filterForm){
        return reportService.getReportsDatatable(filterForm);
    }

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_SALES)
    @Operation(hidden = true)
    public String pageSales(){
        return "/report/sales";
    }

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_PURCHASES)
    @Operation(hidden = true)
    public String pagePurchases(){
        return "/report/purchases";
    }

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_RETURN_CLIENT)
    @Operation(hidden = true)
    public String pageReturnClient(){
        return "/report/returnClient";
    }

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_RETURN_BASE)
    @Operation(hidden = true)
    public String pageReturnBase(){
        return "/report/returnBase";
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_REPORT_SALES)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceForm> getReportSalesDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return reportService.getReportSales(locale, filterForm);
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_REPORT_PURCHASES)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceForm> getReportPurchasesDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return reportService.getReportPurchases(locale, filterForm);
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_REPORT_RETURN_CLIENT)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceForm> getReportReturnClientDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return reportService.getReportReturnClient(locale, filterForm);
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_REPORT_RETURN_BASE)
    @Operation(hidden = true)
    public DataTablesForm<InvoiceForm> getReportReturnBaseDatatable(Locale locale, @RequestBody FilterForm filterForm){
        return reportService.getReportReturnBase(locale, filterForm);
    }
}