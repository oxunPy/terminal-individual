package com.example.rest.service.impl;

import com.example.rest.constant.*;
import com.example.rest.dto.ExchangeRateDto;
import com.example.rest.dto.InvoiceDto;
import com.example.rest.dto.InvoiceItemDto;
import com.example.rest.dto.PagedResponseDto;
import com.example.rest.dto.dtoProjection.InvoiceDtoProjection;
import com.example.rest.dto.dtoProjection.InvoiceItemDtoProjection;
import com.example.rest.dto.dtoProjection.InvoiceProjection;
import com.example.rest.dto.dtoProjection.PagedResponseDtoProjection;
import com.example.rest.model.form.invoice.InvoiceForm;
import com.example.rest.model.form.invoice.InvoiceItemForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.repository.InvoiceRepository;
import com.example.rest.service.CurrencyService;
import com.example.rest.service.InvoiceService;
import com.example.rest.utils.DateUtil;
import com.example.rest.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CurrencyService currencyService;

    @Override
    public Long create(int source, int warehouseId, InvoiceDto invoiceDto, String uid) {
        String typeSource = source == 0 ? TypeSource.DESKTOP.name() : TypeSource.MOBILE.name();

         Long invoiceId = invoiceRepository.
                createInvoice(invoiceDto.getAccountId(),
                        DateUtils.stringToLocalDateFormat(invoiceDto.getDate()),
                        invoiceDto.getType(),
                        invoiceDto.getInfo(),
                        typeSource,
                        Status.TO_PAY.ordinal(), uid);

        int actionType = invoiceDto.getType() == 0 ? 1 : -1;
        ExchangeRateDto lastExchangeRate = currencyService.getLastExchangeRate();

        List<InvoiceItemDto> invoiceItemDtoList = invoiceDto.getInvoiceItemDtoList();

        invoiceItemDtoList.forEach(invoiceItemDto -> {
            invoiceItemDto.setInvoiceId(invoiceId);
            Long invoiceItemId = invoiceRepository.createInvoiceItem(
                    invoiceItemDto.getInvoiceId(),
                    invoiceItemDto.getProductId(),
                    invoiceItemDto.getQuantity(),
                    actionType,
                    Status.ACTIVE.ordinal());

            createInvoiceItemAmount(
                    invoiceItemId,
                    invoiceItemDto.getQuantity().multiply(invoiceItemDto.getSellingRate()),
                    invoiceItemDto.getSellingRate(),
                    invoiceItemDto.getOriginalRate().multiply(lastExchangeRate.getToCurrencyVal()),
                    CurrencyCode.UZS.ordinal() + 1,
                    BigDecimal.valueOf(1),
                    BigDecimal.valueOf(1),
                    InvoiceItemAmountType.MAIN.ordinal(),
                    Status.ACTIVE.ordinal());

            createInvoiceItemAmount(
                    invoiceItemId,
                    invoiceItemDto.getQuantity().multiply(invoiceItemDto.getOriginalRate()),
                    invoiceItemDto.getOriginalRate(),
                    null,
                    CurrencyCode.USD.ordinal() + 1,
                    BigDecimal.valueOf(1),
                    BigDecimal.valueOf(1),
                    InvoiceItemAmountType.STANDART.ordinal(),
                    Status.ACTIVE.ordinal()
            );

            createInvoiceItemAmount(
                    invoiceItemId,
                    invoiceItemDto.getQuantity().multiply(invoiceItemDto.getSellingRate()).divide(lastExchangeRate.getToCurrencyVal(), 4, RoundingMode.HALF_UP),
                    invoiceItemDto.getSellingRate().divide(lastExchangeRate.getToCurrencyVal(), 4, RoundingMode.HALF_UP),
                    invoiceItemDto.getOriginalRate(),
                    CurrencyCode.USD.ordinal() + 1,
                    BigDecimal.valueOf(1),
                    lastExchangeRate.getToCurrencyVal(),
                    InvoiceItemAmountType.OTHER.ordinal(),
                    Status.ACTIVE.ordinal()
            );
        });

        return invoiceId;

    }

    @Override
    public PagedResponseDto<InvoiceDto> getAll(int type, int warehouseId, int source, int page, int size, LocalDate fromDate, LocalDate toDate) {
        String types = type == -1 ? InvoiceType.PRIXOD_BAZA.ordinal() + "," + InvoiceType.RASXOD_KLIENT.ordinal() : String.valueOf(type);
        String typeSource = source == 0 ? TypeSource.DESKTOP.name() : TypeSource.MOBILE.name();
        List<InvoiceDtoProjection> invoiceDtoProjectionList = null;

        if(type == 5) invoiceDtoProjectionList = invoiceRepository.getInvoicesGoDown(typeSource, warehouseId, size, (page - 1) * size, fromDate, toDate);
        else invoiceDtoProjectionList = invoiceRepository.getInvoices(types, warehouseId, typeSource, size, (page - 1) * size, fromDate, toDate, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal());

        List<InvoiceDto> invoiceDtoList = invoiceDtoProjectionList.stream()
                .map(invoiceDtoProjection -> new InvoiceDto(
                        invoiceDtoProjection.getId(),
                        invoiceDtoProjection.getAccount_id(),
                        invoiceDtoProjection.getAccount_printable_name(),
                        invoiceDtoProjection.getDate(),
                        invoiceDtoProjection.getType(),
                        invoiceDtoProjection.getInfo(),
                        invoiceDtoProjection.getStatus(),
                        invoiceDtoProjection.getCount_of_items(),
                        invoiceDtoProjection.getTotal(),
                        null
                )).collect(Collectors.toList());
        PagedResponseDtoProjection invoicesCountAndSum = invoiceRepository.getInvoicesCountAndSum(types, typeSource, fromDate, toDate, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal());
        return new PagedResponseDto<>(invoiceDtoList, invoicesCountAndSum.getCount(), invoicesCountAndSum.getSum());
    }

    @Override
    public List<InvoiceItemDto> getAllItemsById(Long id) {
        List<InvoiceItemDtoProjection> invoiceItemDtoProjectionList = invoiceRepository.getInvoiceItemsByInvoiceId(id, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal());

        return invoiceItemDtoProjectionList.stream().map(invoiceItemDtoProjection ->
                        new InvoiceItemDto(invoiceItemDtoProjection.getId(),
                                null,
                                invoiceItemDtoProjection.getProduct_id(),
                                invoiceItemDtoProjection.getProduct_name(),
                                invoiceItemDtoProjection.getQuantity(),
                                null,
                                invoiceItemDtoProjection.getSelling_rate(),
                                null,
                                invoiceItemDtoProjection.getCurrencyCode(),
                                invoiceItemDtoProjection.getTotal()))
                .collect(Collectors.toList());
    }

    private void createInvoiceItemAmount(Long invoiceItemId, BigDecimal amount, BigDecimal rate, BigDecimal originalRate,
                                         int currencyId, BigDecimal convertion, BigDecimal denominator, int type, int status) {
        invoiceRepository.createInvoiceItemAmount(invoiceItemId, amount, rate, originalRate, currencyId, convertion,
                denominator, type, status);
    }

    public Long update(int source, int warehouseId, InvoiceDto invoiceDto, String uid) {

        InvoiceDto dto = getInvoiceUid(uid);
        Long id;

        if (dto != null) {
            ExchangeRateDto lastExchangeRate = currencyService.getLastExchangeRate();
            int actionType = invoiceDto.getType() == 0 ? 1 : -1;
            String typeSource = source == 0 ? TypeSource.DESKTOP.name() : TypeSource.MOBILE.name();
            id = dto.getId();

            // update invoice and change status to invoice_item and invoice_item_amount
            invoiceRepository.updateInvoice(
                    invoiceDto.getAccountId().intValue(),
                    warehouseId,
                    invoiceDto.getType(),
                    invoiceDto.getInfo(),
                    typeSource,
                    Status.TO_PAY.ordinal(),
                    dto.getId().intValue(),
                    DateUtils.stringToLocalDateFormat(invoiceDto.getDate())
            );

            invoiceRepository.changeInvoiceItemStatus(dto.getId(), Status.DELETED.ordinal());

            invoiceRepository.getInvoiceItemsByInvoiceId(id, Status.ACTIVE.ordinal(),
                    Status.UPDATED.ordinal(), Status.TO_PAY.ordinal())
                    .forEach(invoiceItemDtoProjection -> invoiceRepository.
                            changeInvoiceItemAmountStatus(invoiceItemDtoProjection.getId(), Status.DELETED.ordinal()));

            // create new invoice_item and new invoice_item_amount

            List<InvoiceItemDto> invoiceItemDtoList = invoiceDto.getInvoiceItemDtoList();

            invoiceItemDtoList.forEach(invoiceItemDto -> {
                invoiceItemDto.setInvoiceId(id);
                Long invoiceItemId = invoiceRepository.createInvoiceItem(
                        invoiceItemDto.getInvoiceId(),
                        invoiceItemDto.getProductId(),
                        invoiceItemDto.getQuantity(),
                        actionType,
                        Status.ACTIVE.ordinal());

                createInvoiceItemAmount(
                        invoiceItemId,
                        invoiceItemDto.getQuantity().multiply(invoiceItemDto.getSellingRate()),
                        invoiceItemDto.getSellingRate(),
                        invoiceItemDto.getOriginalRate().multiply(lastExchangeRate.getToCurrencyVal()),
                        CurrencyCode.UZS.ordinal() + 1,
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(1),
                        InvoiceItemAmountType.MAIN.ordinal(),
                        Status.ACTIVE.ordinal());

                createInvoiceItemAmount(
                        invoiceItemId,
                        invoiceItemDto.getQuantity().multiply(invoiceItemDto.getOriginalRate()),
                        invoiceItemDto.getOriginalRate(),
                        null,
                        CurrencyCode.USD.ordinal() + 1,
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(1),
                        InvoiceItemAmountType.STANDART.ordinal(),
                        Status.ACTIVE.ordinal()
                );

                createInvoiceItemAmount(
                        invoiceItemId,
                        invoiceItemDto.getQuantity().multiply(invoiceItemDto.getSellingRate()).divide(lastExchangeRate.getToCurrencyVal(), 4, RoundingMode.HALF_UP),
                        invoiceItemDto.getSellingRate().divide(lastExchangeRate.getToCurrencyVal(), 4, RoundingMode.HALF_UP),
                        invoiceItemDto.getOriginalRate(),
                        CurrencyCode.USD.ordinal() + 1,
                        BigDecimal.valueOf(1),
                        lastExchangeRate.getToCurrencyVal(),
                        InvoiceItemAmountType.OTHER.ordinal(),
                        Status.ACTIVE.ordinal()
                );
            });
            return id;
        }
        return null;
    }

    @Override
    public DataTablesForm<InvoiceForm> getInvoiceDatatable(Locale locale, FilterForm filter) {

        Date fromDate = null;
        Date toDate = null;
        String types = null;
        Integer warehouseId = null;

        Map<String, Object> filterMap = filter.getFilter();
        if(filterMap != null){
            fromDate = DateUtil.parse(MapUtils.getString(filterMap, "fromDate"), DateUtil.PATTERN2);
            toDate = DateUtil.parse(MapUtils.getString(filterMap, "toDate"), DateUtil.PATTERN2);
            types = MapUtils.getString(filterMap, "typeOrdinal");
            warehouseId = MapUtils.getInteger(filterMap, "warehouseId");
        }

        List<InvoiceDtoProjection> invoiceList = invoiceRepository.getInvoices(types, warehouseId, null, filter.getLength(), filter.getStart(), DateUtil.toLocale(fromDate), DateUtil.toLocale(toDate),
                                                                                                Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal());

        List<InvoiceForm> formList = invoiceList.stream().map(i -> new InvoiceForm(i.getId(), i.getAccount_id(), i.getAccount_printable_name(), i.getWarehouseId(), i.getWarehouse(), DateUtil.parse(i.getDate(), DateUtil.PATTERN1), InvoiceType.values()[i.getType()].getLocaleValue(locale), i.getInfo(), i.getCount_of_items(), i.getTotal())).collect(Collectors.toList());

        DataTablesForm<InvoiceForm> dataTable = new DataTablesForm<>();
        dataTable.setRecordsFiltered(invoiceList.size());
        dataTable.setRecordsTotal(invoiceList.size());
        dataTable.setData(formList);
        return dataTable;
    }

    @Override
    public DataTablesForm<InvoiceItemForm> getInvoiceItemsDatatable(Long invoiceId){
        List<InvoiceItemDtoProjection> iiProjectionList = invoiceRepository.getInvoiceItemsByInvoiceId(invoiceId, Status.ACTIVE.ordinal(), Status.UPDATED.ordinal(), Status.TO_PAY.ordinal());

        List<InvoiceItemForm> iiFormList = iiProjectionList.stream().map(ii ->
                new InvoiceItemForm(ii.getId(), ii.getProduct_name(), ii.getQuantity(), null, ii.getSelling_rate(), null, ii.getCurrencyCode(), ii.getTotal())).collect(Collectors.toList());

        DataTablesForm<InvoiceItemForm> dataTable = new DataTablesForm<>();
        dataTable.setData(iiFormList);
        dataTable.setRecordsTotal(iiFormList.size());
        dataTable.setRecordsFiltered(iiFormList.size());
        return dataTable;
    }

    public InvoiceDto getInvoiceUid(String uid) {
        List<InvoiceDtoProjection> projection = invoiceRepository.getInvoiceByUid(uid);
        if(!projection.isEmpty()){
            return new InvoiceDto(
                    projection.get(0).getId(),
                    projection.get(0).getAccount_id(),
                    projection.get(0).getAccount_printable_name(),
                    projection.get(0).getDate(),
                    projection.get(0).getType(),
                    projection.get(0).getInfo(),
                    projection.get(0).getStatus(),
                    projection.get(0).getCount_of_items(),
                    projection.get(0).getTotal(),
                    null
            );
        }
        return null;
    }
}
