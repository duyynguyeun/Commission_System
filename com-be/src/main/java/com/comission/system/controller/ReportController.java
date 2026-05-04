package com.comission.system.controller;

import com.comission.system.dto.response.ApiResponse;
import com.comission.system.dto.response.report.AdminEmployeeRevenueResDTO;
import com.comission.system.dto.response.report.AdminProductRevenueResDTO;
import com.comission.system.dto.response.report.SaleHistoryResDTO;
import com.comission.system.dto.response.report.SaleOverviewResDTO;
import com.comission.system.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/sales/{employeeId}/history")
    public ApiResponse<List<SaleHistoryResDTO>> saleHistory(@PathVariable Long employeeId) {
        return ApiResponse.success(reportService.getSaleHistory(employeeId));
    }

    @GetMapping("/sales/{employeeId}/overview")
    public ApiResponse<SaleOverviewResDTO> saleOverview(@PathVariable Long employeeId) {
        return ApiResponse.success(reportService.getSaleOverview(employeeId));
    }

    @GetMapping("/admin/products/revenue")
    public ApiResponse<List<AdminProductRevenueResDTO>> productRevenue() {
        return ApiResponse.success(reportService.getProductRevenue());
    }

    @GetMapping("/admin/company/revenue")
    public ApiResponse<BigDecimal> companyRevenue() {
        return ApiResponse.success(reportService.getCompanyRevenue());
    }

    @GetMapping("/admin/employees/revenue")
    public ApiResponse<List<AdminEmployeeRevenueResDTO>> employeeRevenue() {
        return ApiResponse.success(reportService.getEmployeeRevenue());
    }
}
