package com.example.rest.utils;

import com.example.rest.dto.ReportByDateDto;
import com.example.rest.dto.ReportTodayIncomeExpenseDto;
import com.example.rest.dto.dtoProjection.ReportByDateProjection;
import com.example.rest.dto.dtoProjection.ReportTodayIncomeExpenseProjection;

public class ConvertProjectionToDto {

    public static ReportByDateDto projectionToDto(ReportByDateProjection reportByDateProjection) {
        ReportByDateDto dto = new ReportByDateDto();
        dto.setInvoiceItemId(reportByDateProjection.getInvoice_item_id());
        dto.setDateOfSell(reportByDateProjection.getDate_of_sell());
        dto.setProductName(reportByDateProjection.getProduct_name());
        dto.setProductPrice(reportByDateProjection.getProduct_price());
        dto.setTypeOfOperation(reportByDateProjection.getType_of_operation());
        dto.setProductQuantity(reportByDateProjection.getProduct_quantity());
        dto.setPriceCurrency(reportByDateProjection.getPrice_currency());

        return dto;
    }

    public static ReportTodayIncomeExpenseDto todayIncomeExpenseProjectionToDto(ReportTodayIncomeExpenseProjection reportTodayIncomeExpenseProjection) {
        ReportTodayIncomeExpenseDto dto = new ReportTodayIncomeExpenseDto();
        dto.setIncome(reportTodayIncomeExpenseProjection.getIncome());
        dto.setExpense(reportTodayIncomeExpenseProjection.getExpense());
        return dto;
    }
}
