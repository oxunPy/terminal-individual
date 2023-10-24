package com.example.rest.service.impl;

import com.example.rest.constant.InvoiceType;
import com.example.rest.constant.Status;
import com.example.rest.constant.TypeSource;
import com.example.rest.dto.ReportByDateDto;
import com.example.rest.dto.ReportTodayIncomeExpenseDto;
import com.example.rest.dto.dtoProjection.InvoiceDtoProjection;
import com.example.rest.dto.dtoProjection.ReportByDateProjection;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.report.ReportByDateForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.repository.InvoiceRepository;
import com.example.rest.repository.ReportRepository;
import com.example.rest.service.ReportService;
import com.example.rest.utils.ConvertProjectionToDto;
import com.example.rest.utils.DateUtil;
import com.example.rest.utils.DateUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             InvoiceRepository invoiceRepository) {
        this.reportRepository = reportRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<ReportByDateDto> getProductsByDate(String from, String to, int size, int page) {
        if (from.isEmpty())
            from = String.valueOf(LocalDate.of(1970, 1, 1));
        if (to.isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
            to = String.valueOf(LocalDate.parse(LocalDate.now().toString(), dateTimeFormatter));
        }
        return reportRepository
                .getInvoiceItemByDate(DateUtils.stringToLocalDateFormat(from), DateUtils.stringToLocalDateFormat(to), size, (page - 1) * size)
                .stream()
                .map(ConvertProjectionToDto::projectionToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReportTodayIncomeExpenseDto getTodaysReport(int source, int warehouseId) {
        String sourceType = source == 0 ? TypeSource.DESKTOP.name() : TypeSource.MOBILE.name();
        return ConvertProjectionToDto.todayIncomeExpenseProjectionToDto(reportRepository.getTodayIncomeExpense(LocalDate.now(), sourceType, warehouseId, 1, -1));
    }

    @Transactional(readOnly = true)
    public DataTablesForm<ReportByDateForm> getReportsDatatable(FilterForm filter){
        Sort sort = getSort(filter.orderMap());

        if(sort.isEmpty()){
            sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "invoice_item_id"));
        }

        String from = null;
        String to = null;

        Map<String, Object> filterMap = filter.getFilter();

        if(filter.getFilter() != null){
            from = StringUtils.getIfEmpty(MapUtils.getString(filterMap, "from"), () -> "1970-01-01");
            to = StringUtils.getIfEmpty(MapUtils.getString(filterMap, "to"), () -> String.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        }

        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<ReportByDateProjection> pageReports = reportRepository.getInvoiceItemByDate(DateUtil.toLocale(DateUtil.parse(from, DateUtil.PATTERN2)), DateUtil.toLocale(DateUtil.parse(to, DateUtil.PATTERN2)), Integer.MAX_VALUE, 0, pageRequest);

        List<ReportByDateForm> reportList = pageReports.stream()
                .map(r -> new ReportByDateForm(r.getInvoice_item_id(), DateUtil.format(DateUtil.fromLocale(r.getDate_of_sell()), "yyyy-MM-dd"), r.getProduct_quantity(), r.getProduct_price(), r.getType_of_operation(), r.getProduct_name(), r.getPrice_currency())).collect(Collectors.toList());

        DataTablesForm<ReportByDateForm> dataTablesForm = new DataTablesForm<>();
        dataTablesForm.setData(reportList);
        dataTablesForm.setDraw(filter.getDraw());
        dataTablesForm.setRecordsTotal((int) pageReports.getTotalElements());
        dataTablesForm.setRecordsFiltered((int) pageReports.getTotalElements());
        return dataTablesForm;
    }

    @Transactional(readOnly = true)
    public DataTablesForm<InvoiceForm> getReportSales(Locale locale, FilterForm filter){
        return getInvoicesByType(locale, filter, InvoiceType.RASXOD_KLIENT);
    }

    @Transactional(readOnly = true)
    public DataTablesForm<InvoiceForm> getReportPurchases(Locale locale, FilterForm filter){
        return getInvoicesByType(locale, filter, InvoiceType.PRIXOD_BAZA);
    }

    @Transactional(readOnly = true)
    public DataTablesForm<InvoiceForm> getReportReturnClient(Locale locale, FilterForm filter){
        return getInvoicesByType(locale, filter, InvoiceType.VOZVRAT_KLIENT);
    }

    @Transactional(readOnly = true)
    public DataTablesForm<InvoiceForm> getReportReturnBase(Locale locale, FilterForm filter){
        return getInvoicesByType(locale, filter, InvoiceType.VOZVRAT_BAZA);
    }

    private Sort getSort(Map<String, OrderForm> orderMap){
        List<Sort.Order> orders = new ArrayList<>();

        if(orderMap != null){
            orderMap.forEach((column, orderForm) -> {
                String property = null;
                switch(column){
                    case "productName" :
                        property = "product_name";
                        break;
                    case "typeOfOperation":
                        property = "type_of_operation";
                        break;
                    case "dateOfSell":
                        property = "date_of_sell";
                }
                Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());
                orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
            });
        }
        return Sort.by(orders);
    }

    private DataTablesForm<InvoiceForm> getInvoicesByType(Locale locale, FilterForm filter, InvoiceType invoiceType){
        Sort sort = getSort(filter.orderMap());

        if(sort.isEmpty()){
            sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        }

        String from = null;
        String to = null;
        Long clientId = null;
        Long warehouseId = null;

        Map<String, Object> filterMap = filter.getFilter();
        if(filterMap != null){
            from = StringUtils.getIfEmpty(MapUtils.getString(filterMap, "fromDate"), () -> "1970-01-01");
            to = StringUtils.getIfEmpty(MapUtils.getString(filterMap, "toDate"), () -> String.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            clientId = MapUtils.getLong(filterMap, "dealerClientId");
            warehouseId = MapUtils.getLong(filterMap, "warehouseId");
        }

        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<InvoiceDtoProjection> pageInvoices = invoiceRepository.getInvoicesPageable(String.valueOf(invoiceType.ordinal()),
                                                                                        DateUtil.toLocale(DateUtil.parse(from, DateUtil.PATTERN2)),
                                                                                        DateUtil.toLocale(DateUtil.parse(to, DateUtil.PATTERN2)),
                                                                                        Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal(),
                                                                                        clientId, warehouseId, pageRequest);

        List<InvoiceForm> invoiceFormList = pageInvoices.stream().map(i -> new InvoiceForm(i.getId(), i.getAccount_id(), i.getAccount_printable_name(),
                i.getWarehouseId(), i.getWarehouse(), DateUtil.parse(i.getDate(), "yyyy-MM-dd"), InvoiceType.values()[i.getType()].getLocaleValue(locale),
                i.getInfo(), i.getCount_of_items(), i.getTotal())).collect(Collectors.toList());

        DataTablesForm<InvoiceForm> dataTable = new DataTablesForm<>();
        dataTable.setData(invoiceFormList);
        dataTable.setRecordsTotal((int) pageInvoices.getTotalElements());
        dataTable.setRecordsTotal((int) pageInvoices.getTotalElements());
        dataTable.setDraw(filter.getDraw());

        return dataTable;
    }


}
