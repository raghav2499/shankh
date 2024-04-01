package com.darzee.shankh.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAmountDAO {
    private Long id;
    private Double totalAmount;
    private Double amountRecieved;
    private OrderDAO order;

    public OrderAmountDAO(Double totalAmount, Double amountRecieved, OrderDAO orderDAO) {
        this.totalAmount = totalAmount;
        this.amountRecieved = amountRecieved;
        this.order = orderDAO;
    }

    public double getStichingCost (){
        return totalAmount - amountRecieved;
    }

    public double getMaterialCost (){
        return amountRecieved;
    }
}
