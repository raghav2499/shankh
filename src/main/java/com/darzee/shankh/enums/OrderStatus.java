package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {

    @JsonProperty("stiching_not_started")
    STITCHING_NOT_STARTED("stiching_not_started", 1),
    @JsonProperty("stitching_in_progress")
    STITCHING_IN_PROGRESS("stitching_in_progress", 2),
    @JsonProperty("order_ready_for_trial")
    ORDER_READY_FOR_TRIAL("order_ready_for_trial", 3),
    @JsonProperty("order_completed")
    ORDER_COMPLETED("order_completed", 4);
    private String orderStatus;

    private Integer ordinal;

    OrderStatus(String orderStatus, Integer ordinal) {
        this.orderStatus = orderStatus;
        this.ordinal = ordinal;
    }

    public String getName() {
        return this.orderStatus;
    }

    public static Map<Integer, OrderStatus> getOrderTypeEnumOrdinalMap() {
        Map<Integer, OrderStatus> orderOrdinalEnumMap = new HashMap<>();
        orderOrdinalEnumMap.put(STITCHING_NOT_STARTED.ordinal, STITCHING_NOT_STARTED);
        orderOrdinalEnumMap.put(STITCHING_NOT_STARTED.ordinal, STITCHING_NOT_STARTED);
        orderOrdinalEnumMap.put(STITCHING_NOT_STARTED.ordinal, STITCHING_NOT_STARTED);
        orderOrdinalEnumMap.put(STITCHING_NOT_STARTED.ordinal, STITCHING_NOT_STARTED);
        return orderOrdinalEnumMap;
    }


}
