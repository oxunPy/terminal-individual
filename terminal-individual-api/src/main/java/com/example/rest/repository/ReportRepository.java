package com.example.rest.repository;

import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.ReportByDateProjection;
import com.example.rest.dto.dtoProjection.ReportTodayIncomeExpenseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM get_invoice_item_by_date(:from_date, :to_date, :items_per_page, :number_of_page)")
    List<ReportByDateProjection> getInvoiceItemByDate(@Param("from_date") LocalDate from_date, @Param("to_date") LocalDate to_date, @Param("items_per_page") int size, @Param("number_of_page") int page);

    @Query(nativeQuery = true, value = "SELECT * FROM get_invoice_item_by_date(:from_date, :to_date, :items_per_page, :number_of_page)",
                    countProjection = "SELECT COUNT(*) FROM get_invoice_item_by_date(:from_date, :to_date, :items_per_page, :number_of_page)")
    Page<ReportByDateProjection> getInvoiceItemByDate(@Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate, @Param("items_per_page") int size, @Param("number_of_page") int page, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM get_today_income_and_expense(:req_date, :type_source, :req_income_type, :req_expense_type, :warehouse_id)")
    ReportTodayIncomeExpenseProjection getTodayIncomeExpense(@Param("req_date") LocalDate reqDate,
                                                             @Param("type_source") String typeSource,
                                                             @Param("warehouse_id") int warehouseId,
                                                             @Param(("req_income_type")) int incomeType,
                                                             @Param(("req_expense_type")) int expenseType);


}
