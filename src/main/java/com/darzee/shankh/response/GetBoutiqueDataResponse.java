package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetBoutiqueDataResponse {
    private LedgerData monthlyLedgerData;
    private LedgerData overallLedgerData;

    public class LedgerData {
        public Double amountRecieved;
        public Double pendingAmount;

        public Integer closedOrderCount;

        public Integer activeOrderCount;

        public LedgerData(Double amountRecieved, Double pendingAmount, Integer activeOrderCount,
                          Integer closedOrderCount) {
            this.amountRecieved = amountRecieved;
            this.pendingAmount = pendingAmount;
            this.activeOrderCount = activeOrderCount;
            this.closedOrderCount = closedOrderCount;
        }
    }

    public void setMonthlyLedgerData(Double amountRecieved, Double pendingAmount, Integer activeOrder, Integer closedOrder) {
        this.monthlyLedgerData =  new LedgerData(amountRecieved, pendingAmount, activeOrder, closedOrder);
    }

    public void setOverallLedgerData(Double amountRecieved, Double pendingAmount, Integer activeOrder, Integer closedOrder) {
        this.overallLedgerData = new LedgerData(amountRecieved, pendingAmount, activeOrder, closedOrder);
    }

}
