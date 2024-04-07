package com.darzee.shankh.response;

public class invoiceOrderSummaryItem {
    String itemName ;
    double stichingCost ;
    double materialCost ;
    String expectedDeliveryDate ;
    double quantity;
    
    public invoiceOrderSummaryItem(String itemName, double stichingCost, double materialCost, String expectedDeliveryDate) {
        this.itemName = itemName;
        this.stichingCost = stichingCost;
        this.materialCost = materialCost;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
