package com.darzee.shankh.dao;

import com.darzee.shankh.request.PriceBreakUpDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceBreakupDAO {

    private Long id;
    private String component;
    private Double value = 0d;
    private Integer quantity = 1;

    private Boolean isDeleted = Boolean.FALSE;
    private OrderItemDAO orderItem;

    public PriceBreakupDAO(PriceBreakUpDetails priceBreakUpDetails, OrderItemDAO orderItem) {
        this.component = priceBreakUpDetails.getComponent();
        this.value = priceBreakUpDetails.getValue();
        this.quantity = priceBreakUpDetails.getComponentQuantity();
        this.isDeleted = priceBreakUpDetails.getIsDeleted();
        this.orderItem = orderItem;
    }

    public Double getTotalBreakUpValue() {
        return this.getValue() * this.getQuantity();
    }

    public boolean isComponentStringUpdated(String value) {
        return value != null && !this.component.equals(value);
    }

    public boolean isValueUpdated(Double value) {
        return value != null && !this.value.equals(value);
    }

    public boolean isQuantityUpdated(Integer value) {
        return value != null && !this.quantity.equals(value);
    }
}
