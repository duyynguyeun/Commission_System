package com.comission.system.service;

import com.comission.system.dto.response.report.AdminEmployeeRevenueResDTO;
import com.comission.system.dto.response.report.AdminProductRevenueResDTO;
import com.comission.system.dto.response.report.SaleHistoryResDTO;
import com.comission.system.dto.response.report.SaleOverviewResDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ReportService {
    List<SaleHistoryResDTO> getSaleHistory(Long employeeId);
    SaleOverviewResDTO getSaleOverview(Long employeeId);
    List<AdminProductRevenueResDTO> getProductRevenue();
    BigDecimal getCompanyRevenue();
    List<AdminEmployeeRevenueResDTO> getEmployeeRevenue();
}
