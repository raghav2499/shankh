package com.darzee.shankh.response;

import com.darzee.shankh.dao.OrderItemDAO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemSummary {
    private String outfitType;
    private String trialDate;
    private String deliveryDate;

    private Integer quantity;

    public OrderItemSummary(OrderItemDAO orderItem) {
        this.deliveryDate = orderItem.getDeliveryDate().toString();
        this.outfitType = orderItem.getOutfitType().getDisplayString();
        this.trialDate = orderItem.getTrialDate() != null ? orderItem.getTrialDate().toString() : null;
        this.quantity = orderItem.getQuantity();
    }
}