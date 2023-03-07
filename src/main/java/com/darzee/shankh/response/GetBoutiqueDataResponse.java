package com.darzee.shankh.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetBoutiqueDataResponse {

    private LedgerData weeklyLedgerData;
    private LedgerData monthlyLedgerData;
    private LedgerData overallLedgerData;

    public class LedgerData {
        public Double amountRecieved;
        public Double pendingAmount;

        public LedgerData(Double amountRecieved, Double pendingAmount) {
            this.amountRecieved = amountRecieved;
            this.pendingAmount = pendingAmount;
        }
    }

    private Integer closedOrderCount;
    private Integer activeOrderCount;
    private String ownerTailorName;

    public void setMonthlyLedgerData(Double amountRecieved, Double pendingAmount) {
        this.monthlyLedgerData =  new LedgerData(amountRecieved, pendingAmount);
    }

    public void setWeeklyLedgerData(Double amountRecieved, Double pendingAmount) {
        this.weeklyLedgerData = new LedgerData(amountRecieved, pendingAmount);
    }

    public void setOverallLedgerData(Double amountRecieved, Double pendingAmount) {
        this.overallLedgerData = new LedgerData(amountRecieved, pendingAmount);
    }

}
