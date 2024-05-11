package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderItemStatus {

    @JsonProperty("accepted")
    ACCEPTED("accepted", 1, "Accepted"),
    @JsonProperty("under_cutting")
    UNDER_CUTTING("under_cutting", 2, "Under Cutting"),
    @JsonProperty("under_finishing")
    UNDER_FINISHING("under_finishing", 3, "Under Finishing"),
    @JsonProperty("completed")
    COMPLETED("completed", 4, "Completed"),

    @JsonProperty("delivered")
    DELIVERED("delivered", 5, "Delivered"),

    @JsonProperty("under_stitching")
    UNDER_STITCHING("under_stitching", 6, "Under Stitching"),

    @JsonProperty("drafted")
    DRAFTED("drafted", 7, "Drafted");
    private String orderItemStatus;

    private Integer ordinal;
    private String displayString;

    OrderItemStatus(String orderItemStatus, Integer ordinal, String displayString) {
        this.orderItemStatus = orderItemStatus;
        this.ordinal = ordinal;
        this.displayString = displayString;
    }

    public String getName() {
        return this.orderItemStatus;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }
    public String getDisplayString() {
        return this.displayString;
    }

    public static Map<Integer, OrderItemStatus> getOrderItemStatusEnumOrdinalMap() {
        Map<Integer, OrderItemStatus> orderItemOrdinalEnumMap = new HashMap<>();
        for(OrderItemStatus status : OrderItemStatus.values()) {
            orderItemOrdinalEnumMap.put(status.getOrdinal(), status);
        }
        return orderItemOrdinalEnumMap;
    }
}

