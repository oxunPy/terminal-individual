package com.example.rest.service;

import com.example.rest.dto.InvoiceDto;
import com.example.rest.dto.InvoiceItemDto;
import com.example.rest.dto.PagedResponseDto;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.invoice.InvoiceItemForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface InvoiceService {

    Long create(int source, int warehouseId, InvoiceDto invoiceDto, String uid);

    PagedResponseDto<InvoiceDto> getAll(int type, int warehouseId, int source, int page, int size, LocalDate fromDate, LocalDate toDate);

    List<InvoiceItemDto> getAllItemsById(Long id);

    InvoiceDto getInvoiceUid(String uid);


    Long update(int source, int warehouseId, InvoiceDto invoiceDto, String uid);

    DataTablesForm<InvoiceForm> getInvoiceDatatable(Locale locale, FilterForm filterForm);

    DataTablesForm<InvoiceItemForm> getInvoiceItemsDatatable(Long invoiceId);
}
