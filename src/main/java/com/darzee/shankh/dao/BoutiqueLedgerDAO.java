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
    private Double totalAmountRecieved = 0d;
    private Double totalPendingAmount = 0d;
    private Integer monthlyActiveOrders = 0;
    private Integer monthlyClosedOrders = 0;
    private Integer totalActiveOrders = 0;
    private Integer totalClosedOrders = 0;

    public BoutiqueLedgerDAO(Long boutiqueId) {
        this.boutiqueId = boutiqueId;
    }

    public void addOrderAmountToBoutiqueLedger(Double pendingAmount, Double amountRecieved) {
        this.setTotalAmountRecieved(this.getTotalAmountRecieved() + amountRecieved);
        this.setMonthlyAmountRecieved(this.getMonthlyAmountRecieved() + amountRecieved);
        this.setTotalPendingAmount(this.getTotalPendingAmount() + pendingAmount);
        this.setMonthlyPendingAmount(this.getMonthlyPendingAmount() + pendingAmount);
    }

    public void incrementActiveOrdersInLedger() {
        this.setMonthlyActiveOrders(this.getMonthlyActiveOrders() + 1);
        this.setTotalActiveOrders(this.getTotalActiveOrders() + 1);
    }

    public void incrementClosedOrdersInLedger() {
        this.setMonthlyClosedOrders(this.getMonthlyClosedOrders() + 1);
        this.setTotalClosedOrders(this.getTotalClosedOrders() + 1);
        this.setMonthlyActiveOrders(this.getMonthlyActiveOrders() - 1);
        this.setTotalActiveOrders(this.getTotalActiveOrders() - 1);
    }
}
