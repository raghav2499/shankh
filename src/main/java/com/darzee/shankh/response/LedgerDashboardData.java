package com.darzee.shankh.response;

import com.darzee.shankh.dao.BoutiqueLedgerSnapshotDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LedgerDashboardData {
    public Double amountRecieved;
    public Double pendingAmount;

    public Integer closedOrderCount;

    public Integer activeOrderCount;

    public LedgerDashboardData(Double amountRecieved, Double pendingAmount, Integer activeOrderCount,
                               Integer closedOrderCount) {
        this.amountRecieved = amountRecieved;
        this.pendingAmount = pendingAmount;
        this.activeOrderCount = activeOrderCount;
        this.closedOrderCount = closedOrderCount;
    }

    public LedgerDashboardData(BoutiqueLedgerSnapshotDAO ledgerSnapshot) {
        this.amountRecieved = ledgerSnapshot.getAmountRecieved();;
        this.pendingAmount = ledgerSnapshot.getPendingAmount();
        this.activeOrderCount = ledgerSnapshot.getActiveOrderCount();
    }
}
