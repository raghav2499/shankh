package com.darzee.shankh.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderItemSort {

    TRIAL_DATE("trialDate"),

    DELIVERY_DATE("deliveryDate"),
    CREATED_AT("createdAt"),
    ITEM_ID("id");

    private String orderItemSortString;

    OrderItemSort(String orderItemSortString) {
        this.orderItemSortString = orderItemSortString;
    }

        static Map<String, OrderItemSort> orderSortMap = getEnumMap();

    public static Map<String, OrderItemSort> getEnumMap() {
        Map<String, OrderItemSort> orderSortEnumMap = new HashMap<>();
        orderSortEnumMap.put(OrderItemSort.DELIVERY_DATE.orderItemSortString, OrderItemSort.DELIVERY_DATE);
        orderSortEnumMap.put(OrderItemSort.TRIAL_DATE.orderItemSortString, OrderItemSort.TRIAL_DATE);
        return orderSortEnumMap;
    }

    public String getOrderSortString() {
        return this.orderItemSortString;
    }
}
