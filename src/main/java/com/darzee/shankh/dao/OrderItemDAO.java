package com.darzee.shankh.dao;

import com.darzee.shankh.enums.OrderType;
import com.darzee.shankh.enums.OutfitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDAO {
    private Long id;

    private LocalDateTime trialDate;

    private LocalDateTime deliveryDate;

    private String specialInstructions;

    private OrderType orderType;

    private OutfitType outfitType;

    private String inspiration;

    private Boolean isPriorityOrder;

    private Integer quantity;

    private List<PriceBreakupDAO> priceBreakup;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private OrderDAO order;

    public OrderItemDAO(LocalDateTime trialDate, LocalDateTime deliveryDate, String specialInstructions,
                        OrderType orderType, OutfitType outfitType, String inspiration, Boolean isPriorityOrder,
                        Integer quantity, OrderDAO orderDAO) {
        this.trialDate = trialDate;
        this.deliveryDate = deliveryDate;
        this.specialInstructions = specialInstructions;
        this.orderType = orderType;
        this.outfitType = outfitType;
        this.inspiration = inspiration;
        this.isPriorityOrder = isPriorityOrder;
        this.quantity = quantity;
        this.order = orderDAO;
    }

    public boolean isTrialDateUpdated(LocalDateTime value) {
        return value != null && !value.equals(this.getTrialDate());
    }

    public boolean isDeliveryDateUpdated(LocalDateTime value) {
        return value != null && !value.equals(this.getDeliveryDate());
    }

    public boolean areSpecialInstructionsUpdated(String value) {
        return value != null && !value.equals(this.getSpecialInstructions());
    }

    public boolean isInspirationUpdated(String value) {
        return value != null && !value.equals(this.getInspiration());
    }


    public boolean isPriorityUpdated(Boolean value) {
        return value != null && !value.equals(this.getIsPriorityOrder());
    }

    public boolean isQuantityUpdated(Integer value) {
        return value != null && !value.equals(this.getQuantity());
    }

}
