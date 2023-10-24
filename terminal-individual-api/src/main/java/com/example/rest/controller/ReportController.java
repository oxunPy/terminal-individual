package com.example.rest.controller;

import com.example.rest.dto.ReportByDateDto;
import com.example.rest.dto.ReportTodayIncomeExpenseDto;
import com.example.rest.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@SecurityRequirement(name = "apiV1")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportByDateDto>> getReportByDate(
            @RequestParam(value = "from", required = false, defaultValue = "") String from,
            @RequestParam(value = "to", required = false, defaultValue = "") String to,
            @RequestParam(value = "itemsPerPage", required = false, defaultValue = "5") int itemsPerPage,
            @RequestParam(value = "numberOfPage", required = false, defaultValue = "1") int numberOfPage) {
        return ResponseEntity.ok().body(reportService.getProductsByDate(from, to, itemsPerPage, numberOfPage));
    }

    @GetMapping("/today")
    public ResponseEntity<ReportTodayIncomeExpenseDto> getTodayIncomeExpenseReport(@RequestParam int source, @RequestParam int warehouseId) {
        return ResponseEntity.ok().body(reportService.getTodaysReport(source, warehouseId));
    }
}
