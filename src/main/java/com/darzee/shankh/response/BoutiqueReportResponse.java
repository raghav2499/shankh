package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoutiqueReportResponse {

    String message;

    LedgerDashboardData ledgerDashboardData = new LedgerDashboardData();
    CustomerDashboard customerDashboard = new CustomerDashboard();
    List<TopCustomerData> topCustomerData = new ArrayList<>();
    SalesDashboard weekwiseSalesSplit;
    List<OrderTypeDashboardData> orderTypeSalesSplit;

    Integer activeSinceMonth;
    Integer activeSinceYear;

    Boolean isActive;

    public BoutiqueReportResponse(LedgerDashboardData ledgerDashboardData,
                                  CustomerDashboard customerDashboard,
                                  List<TopCustomerData> topCustomerData,
                                  SalesDashboard weekwiseSalesSplit,
                                  List<OrderTypeDashboardData> orderTypeSalesSplit,
                                  Integer activeSinceMonth,
                                  Integer activeSinceYear,
                                  Boolean isActive,
                                  String message) {
        this.ledgerDashboardData = ledgerDashboardData;
        this.customerDashboard = customerDashboard;
        this.topCustomerData = topCustomerData;
        this.weekwiseSalesSplit = weekwiseSalesSplit;
        this.orderTypeSalesSplit = orderTypeSalesSplit;
        this.activeSinceMonth = activeSinceMonth;
        this.activeSinceYear = activeSinceYear;
        this.isActive = isActive;
        this.message = message;
    }
}
