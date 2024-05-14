package com.darzee.shankh.response;

import com.darzee.shankh.dao.BoutiqueLedgerSnapshotDAO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class LedgerDashboardData {
    public Double amountRecieved = 0d;
    public Double pendingAmount = 0d;
    public Integer activeOrderCount = 0;
    public Integer closedOrderCount = 0;

    public LedgerDashboardData(BoutiqueLedgerSnapshotDAO ledgerSnapshot, Double pendingAmount, Integer activeOrderCount) {
        this.amountRecieved = ledgerSnapshot.getAmountRecieved();
        this.pendingAmount = pendingAmount;
        this.activeOrderCount = activeOrderCount;
    }
}
