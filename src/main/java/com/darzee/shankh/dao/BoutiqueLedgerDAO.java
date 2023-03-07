package com.darzee.shankh.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoutiqueLedgerDAO {

    private Long id;
    private Long boutiqueId;
    private Double monthlyAmountRecieved = 0d;
    private Double monthlyPendingAmount = 0d;
    private Double weeklyAmountRecieved = 0d;
    private Double weeklyPendingAmount = 0d;
    private Double totalAmountRecieved = 0d;
    private Double totalPendingAmount = 0d;

    public BoutiqueLedgerDAO(Long boutiqueId) {
        this.boutiqueId = boutiqueId;
    }

    public void addOrderAmountToBoutiqueLedger(Double pendingAmount, Double amountRecieved) {
        this.setTotalAmountRecieved(this.getTotalAmountRecieved() + amountRecieved);
        this.setMonthlyAmountRecieved(this.getMonthlyAmountRecieved() + amountRecieved);
        this.setWeeklyAmountRecieved(this.getWeeklyAmountRecieved() + amountRecieved);
        this.setTotalPendingAmount(this.getTotalPendingAmount() + pendingAmount);
        this.setMonthlyPendingAmount(this.getMonthlyPendingAmount() + pendingAmount);
        this.setWeeklyPendingAmount(this.getWeeklyPendingAmount() + pendingAmount);
    }
}
