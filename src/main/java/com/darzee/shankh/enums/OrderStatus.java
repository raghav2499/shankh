package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {

    @JsonProperty("stiching_not_started")
    STITCHING_NOT_STARTED("stiching_not_started"),
    @JsonProperty("stitching_in_progress")
    STITCHING_IN_PROGRESS("stitching_in_progress"),
    @JsonProperty("order_ready_for_trial")
    ORDER_READY_FOR_TRIAL("order_ready_for_trial"),
    @JsonProperty("order_completed")
    ORDER_COMPLETED("order_completed");
    private String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
