package com.example.rest.service;

import com.example.rest.dto.ReportByDateDto;
import com.example.rest.dto.ReportTodayIncomeExpenseDto;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.report.ReportByDateForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;

import java.util.List;
import java.util.Locale;

public interface ReportService {
    List<ReportByDateDto> getProductsByDate(String from_date, String to_date, int itemsPerPage, int numberOfPage);

    ReportTodayIncomeExpenseDto getTodaysReport(int source, int warehouseId);

    DataTablesForm<ReportByDateForm> getReportsDatatable(FilterForm filterForm);

    DataTablesForm<InvoiceForm> getReportSales(Locale locale, FilterForm filterForm);

    DataTablesForm<InvoiceForm> getReportPurchases(Locale locale, FilterForm filterForm);

    DataTablesForm<InvoiceForm> getReportReturnClient(Locale locale, FilterForm filterForm);

    DataTablesForm<InvoiceForm> getReportReturnBase(Locale locale, FilterForm filterForm);

}
