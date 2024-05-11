package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {

    @JsonProperty("drafted")
    DRAFTED("drafted", 1, "Drafted"),

    @JsonProperty("accepted")
    ACCEPTED("accepted", 2, "Accepted"),

    @JsonProperty("delivered")
    DELIVERED("delivered", 3, "Delivered");

    private String orderStatus;

    private Integer ordinal;
    private String displayString;

    OrderStatus(String orderStatus, Integer ordinal, String displayString) {
        this.orderStatus = orderStatus;
        this.ordinal = ordinal;
        this.displayString = displayString;
    }

    public String getDisplayString() {
        return displayString;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public static Map<Integer, OrderStatus> getOrderStatusEnumOrdinalMap() {
        Map<Integer, OrderStatus> orderStatusEnumOrdinalMap = new HashMap<>();
        for(OrderStatus status : OrderStatus.values()) {
            orderStatusEnumOrdinalMap.put(status.getOrdinal(), status);
        }
        return orderStatusEnumOrdinalMap;
    }
}
