package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {

    @JsonProperty("stiching_not_started")
    STITCHING_NOT_STARTED("stiching_not_started", 1, "Stitching not started"),
    @JsonProperty("stitching_in_progress")
    STITCHING_IN_PROGRESS("stitching_in_progress", 2, "Stitching in progress"),
    @JsonProperty("order_ready_for_trial")
    ORDER_READY_FOR_TRIAL("order_ready_for_trial", 3, "Ready for Trial"),
    @JsonProperty("order_completed")
    ORDER_COMPLETED("order_completed", 4, "Order Completed"),

    @JsonProperty("order_delivered")
    ORDER_DELIVERED("order_delivered", 5, "Order Delivered");
    private String orderStatus;

    private Integer ordinal;
    private String displayString;

    OrderStatus(String orderStatus, Integer ordinal, String displayString) {
        this.orderStatus = orderStatus;
        this.ordinal = ordinal;
        this.displayString = displayString;
    }

    public String getName() {
        return this.orderStatus;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }
    public String getDisplayString() {
        return this.displayString;
    }

    public static Map<Integer, OrderStatus> getOrderTypeEnumOrdinalMap() {
        Map<Integer, OrderStatus> orderOrdinalEnumMap = new HashMap<>();
        orderOrdinalEnumMap.put(STITCHING_NOT_STARTED.ordinal, STITCHING_NOT_STARTED);
        orderOrdinalEnumMap.put(STITCHING_IN_PROGRESS.ordinal, STITCHING_IN_PROGRESS);
        orderOrdinalEnumMap.put(ORDER_READY_FOR_TRIAL.ordinal, ORDER_READY_FOR_TRIAL);
        orderOrdinalEnumMap.put(ORDER_COMPLETED.ordinal, ORDER_COMPLETED);
        orderOrdinalEnumMap.put(ORDER_DELIVERED.ordinal, ORDER_DELIVERED);
        return orderOrdinalEnumMap;
    }
}
