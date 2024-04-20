package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBoutiqueLedgerDataResponse {
    private LedgerDashboardData monthlyLedgerDashboardData;
    private LedgerDashboardData overallLedgerDashboardData;

    public void setMonthlyLedgerDashboardData(Double amountRecieved, Double pendingAmount, Integer activeOrder, Integer closedOrder) {
        this.monthlyLedgerDashboardData = new LedgerDashboardData(amountRecieved, pendingAmount, activeOrder, closedOrder);
    }

    public void setOverallLedgerDashboardData(Double amountRecieved, Double pendingAmount, Integer activeOrder, Integer closedOrder) {
        this.overallLedgerDashboardData = new LedgerDashboardData(amountRecieved, pendingAmount, activeOrder, closedOrder);
    }

}
