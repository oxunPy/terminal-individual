package com.example.rest.controller;

import com.example.rest.dto.InvoiceDto;
import com.example.rest.dto.InvoiceItemDto;
import com.example.rest.dto.PagedResponseDto;
import com.example.rest.service.InvoiceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@SecurityRequirement(name = "apiV1")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Long> createInvoice(@RequestParam int source, @RequestParam int warehouseId, @RequestParam String uid, @RequestBody @Valid InvoiceDto invoiceDto) {
        if(invoiceService.getInvoiceUid(uid) == null)
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.create(source, warehouseId, invoiceDto, uid));
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.update(source, warehouseId, invoiceDto, uid));

    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<InvoiceDto>> getInvoices(@RequestParam int type,
                                                                    @RequestParam(value = "warehouseId", required = false) int warehouseId,
                                                                    @RequestParam int source,
                                                                    @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                                                    @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                    @RequestParam(name = "from", defaultValue = "1970-01-01", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fromDate,
                                                                    @RequestParam(name = "to", defaultValue = "2100-01-01", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate toDate) {
        return ResponseEntity.ok(invoiceService.getAll(type, warehouseId, source, page, size, fromDate, toDate));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<InvoiceItemDto>> getInvoiceItems(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getAllItemsById(id));
    }
}
