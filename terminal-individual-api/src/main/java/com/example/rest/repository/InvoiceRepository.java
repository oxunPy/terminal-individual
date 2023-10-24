package com.example.rest.repository;

import com.example.rest.constant.InvoiceType;
import com.example.rest.constant.Status;
import com.example.rest.dto.dtoProjection.InvoiceProjection;
import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.InvoiceDtoProjection;
import com.example.rest.dto.dtoProjection.InvoiceItemDtoProjection;
import com.example.rest.dto.dtoProjection.PagedResponseDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<RootEntity, Long> {


    @Query(nativeQuery = true, value = "select create_invoice(:account_id, :date, :type, :info, :type_source, :status, :uid)")
    Long createInvoice(@Param("account_id") Long accountId,
                       @Param("date") LocalDate date,
                       @Param("type") int type,
                       @Param("info") String info,
                       @Param("type_source") String typeSource,
                       @Param("status") int status,
                       @Param("uid") String uid);


    @Query(nativeQuery = true, value = "select create_invoice_item(:invoice_id, :product_id, :qty, :action_type, :status)")
    Long createInvoiceItem(@Param("invoice_id") Long invoiceId,
                           @Param("product_id") Long productId,
                           @Param("qty") BigDecimal quantity,
                           @Param("action_type") int actionType,
                           @Param("status") int status);


    @Query(nativeQuery = true, value = "select create_invoice_item_amount(:invoice_item_id, :amount, :rate, :original_rate, :currency_id, :convertion, :denominator, :type, :status)")
    Long createInvoiceItemAmount(@Param("invoice_item_id") Long invoiceItemId,
                                 @Param("amount") BigDecimal amount,
                                 @Param("rate") BigDecimal sellingRate,
                                 @Param("original_rate") BigDecimal originalRate,
                                 @Param("currency_id") int currencyId,
                                 @Param("convertion") BigDecimal conversion,
                                 @Param("denominator") BigDecimal denominator,
                                 @Param("type") int type,
                                 @Param("status") int status);

    @Query(nativeQuery = true, value = "select * from get_invoices(:types, :type_source, :lim, :offs, :from_date, :to_date, :active, :updated, :to_pay, :warehous_id, null)")
    List<InvoiceDtoProjection> getInvoices(@Param("types") String types,
                                           @Param("warehous_id") Integer warehouseId,
                                           @Param("type_source") String typeSource,
                                           @Param("lim") int limit,
                                           @Param("offs") int offset,
                                           @Param("from_date") LocalDate fromDate,
                                           @Param("to_date") LocalDate toDate,
                                           @Param("active") int active,
                                           @Param("updated") int updated,
                                           @Param("to_pay") int toPay);

    @Query(nativeQuery = true, value = "select * from get_invoices(:types, null," + Integer.MAX_VALUE + ", 0, :from_date, :to_date, :active, :updated, :to_pay, :warehouse_id, :client_id)",
                               countProjection = "select count(*) get_invoices(:types, null, 0,"  + Integer.MAX_VALUE + ",  :from_date, :to_date, :active, :updated, :to_pay, :warehouse_id, :client_id)")
    Page<InvoiceDtoProjection> getInvoicesPageable(@Param("types") String types,
                                                   @Param("from_date") LocalDate fromDate,
                                                   @Param("to_date") LocalDate toDate,
                                                   @Param("active") int active,
                                                   @Param("updated") int updated,
                                                   @Param("to_pay") int toPay,
                                                   @Param("client_id") Long dealerClientId,
                                                   @Param("warehouse_id") Long warehouseId,
                                                   Pageable pageable);

    @Query(nativeQuery = true, value = "select * from get_invoices_go_down(:type_source, :lim, :offs, :from_date, :to_date, :warehous_id)")
    List<InvoiceDtoProjection> getInvoicesGoDown(@Param("type_source") String typeSource,
                                                 @Param("warehous_id") Integer warehouseId,
                                                 @Param("lim") int limit,
                                                 @Param("offs") int offset,
                                                 @Param("from_date") LocalDate fromDate,
                                                 @Param("to_date") LocalDate toDate);

    @Query(nativeQuery = true, value = "select * from get_invoices_count_and_sum(:types, :type_source, :from_date, :to_date, :active, :updated, :to_pay)")
    PagedResponseDtoProjection getInvoicesCountAndSum(@Param("types") String types,
                                                      @Param("type_source") String typeSource,
                                                      @Param("from_date") LocalDate fromDate,
                                                      @Param("to_date") LocalDate toDate,
                                                      @Param("active") int active,
                                                      @Param("updated") int updated,
                                                      @Param("to_pay") int toPay);

    @Query(nativeQuery = true, value = "select * from get_invoice_items_by_invoice_id(:invoice_id, :active, :updated, :to_pay)")
    List<InvoiceItemDtoProjection> getInvoiceItemsByInvoiceId(@Param("invoice_id") Long invoiceId,
                                                              @Param("active") int active,
                                                              @Param("updated") int updated,
                                                              @Param("to_pay") int toPay);
    @Query(nativeQuery = true, value = "select * from get_invoice_by_uid(:uid)")
    List<InvoiceDtoProjection> getInvoiceByUid(@Param("uid") String uid);


    @Query(nativeQuery = true, value = "select update_invoice(:dealer_client_id, :warehouse_id, :type, :info, :type_source, :status, :invoice_id, :date)")
    Long  updateInvoice(@Param("dealer_client_id") int dealer_client_id,
                       @Param("warehouse_id") int warehouseId,
                       @Param("type") int type,
                       @Param("info") String info,
                       @Param("type_source") String typeSource,
                       @Param("status") int status,
                       @Param("invoice_id") Integer invoiceId,
                       @Param("date") LocalDate date);

    @Query(nativeQuery = true, value = "select cast(change_invoice_item_status(:invoice_id, :status) as text)")
    List<String> changeInvoiceItemStatus(@Param("invoice_id") Long invoiceId, @Param("status") int status);

    @Query(nativeQuery = true, value = "select cast(change_invoice_item_amount_status(:invoice_item_id, :status) as text)")
    List<String> changeInvoiceItemAmountStatus(@Param("invoice_item_id") Long invoiceItemId, @Param("status") int status);

    @Query(nativeQuery = true, value = "SELECT " +
                                       "    i.id as id,\n" +
                                       "    i.client_id as clientId,\n" +
                                       "    dc.printable_name as printableName,\n" +
                                       "    i.date as date,\n" +
                                       "    i.type as type,\n" +
                                       "    i.info as info,\n" +
                                       "    count(i.id) as itemCount,\n" +
                                       "    sum(iia.amount) as total\n" +
                                       "FROM invoice i\n" +
                                       "INNER JOIN dealer_client dc on dc.id = i.client_id\n" +
                                       "INNER JOIN invoice_item ii on i.id = ii.invoice_id\n" +
                                       "INNER JOIN invoice_item_amount iia on ii.id = iia.invoice_item\n" +
                                       "GROUP BY i.id, dc.printable_name\n" +
                                       "ORDER BY i.id DESC")
    List<InvoiceProjection> getAllInvoices();
}
