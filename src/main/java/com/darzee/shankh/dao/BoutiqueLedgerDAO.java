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

    public void decrementActiveOrderCount(Integer decrValue) {
        this.setTotalActiveOrders(Math.max(this.getTotalActiveOrders() - decrValue, 0));
        this.setMonthlyActiveOrders(Math.max(this.getMonthlyActiveOrders() - decrValue, 0));
    }

    public void decrementClosedOrderCount(Integer decrValue) {
        this.setTotalClosedOrders(Math.max(this.getTotalClosedOrders() - decrValue, 0));
        this.setMonthlyClosedOrders(Math.max(this.getMonthlyClosedOrders() - decrValue, 0));
    }

    public void incrementActiveOrderCount(Integer incrValue) {
        this.setTotalActiveOrders(Math.max(this.getTotalActiveOrders() + incrValue, 0));
        this.setMonthlyActiveOrders(Math.max(this.getMonthlyActiveOrders() + incrValue, 0));
    }

    public void incrementClosedOrderCount(Integer incrValue) {
        this.setTotalClosedOrders(Math.max(this.getTotalClosedOrders() + incrValue, 0));
        this.setMonthlyClosedOrders(Math.max(this.getMonthlyClosedOrders() + incrValue, 0));
    }

    public void resetMonthlyDetailsInLedger() {
        this.setMonthlyPendingAmount(this.getTotalPendingAmount);
        this.setMonthlyAmountRecieved(0d);
        this.setMonthlyActiveOrders(this.getTotalActiveOrders());
        this.setMonthlyClosedOrders(0);
    }
}
