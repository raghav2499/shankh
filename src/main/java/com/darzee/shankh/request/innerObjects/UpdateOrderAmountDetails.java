package com.darzee.shankh.request.innerObjects;

import lombok.Data;

@Data
public class UpdateOrderAmountDetails {
    private Double totalOrderAmount;
    private Double advanceOrderAmount;
    private Double dueBalance;
}
