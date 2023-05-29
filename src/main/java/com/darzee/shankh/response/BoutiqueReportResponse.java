package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoutiqueReportResponse {

    String message;

    LedgerDashboardData ledgerDashboardData;
    CustomerDashboard customerDashboard;
    List<TopCustomerData> topCustomerData;
    SalesDashboard weekwiseSalesSplit;
    List<OrderTypeDashboardData> orderTypeSalesSplit;

    public BoutiqueReportResponse(LedgerDashboardData ledgerDashboardData,
                                  CustomerDashboard customerDashboard,
                                  List<TopCustomerData> topCustomerData,
                                  SalesDashboard weekwiseSalesSplit,
                                  List<OrderTypeDashboardData> orderTypeSalesSplit,
                                  String message) {
        this.ledgerDashboardData = ledgerDashboardData;
        this.customerDashboard = customerDashboard;
        this.topCustomerData = topCustomerData;
        this.weekwiseSalesSplit = weekwiseSalesSplit;
        this.orderTypeSalesSplit = orderTypeSalesSplit;
        this.message = message;
    }
}
