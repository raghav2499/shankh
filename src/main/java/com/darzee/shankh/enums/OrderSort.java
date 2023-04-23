package com.darzee.shankh.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderSort {

    TRIAL_DATE("trialDate"),

    DELIVERY_DATE("deliveryDate");

    private String orderSortString;

    OrderSort(String orderSortString) {
        this.orderSortString = orderSortString;
    }

        static Map<String, OrderSort> orderSortMap = getEnumMap();

    public static Map<String, OrderSort> getEnumMap() {
        Map<String, OrderSort> orderSortEnumMap = new HashMap<>();
        orderSortEnumMap.put(OrderSort.DELIVERY_DATE.orderSortString, OrderSort.DELIVERY_DATE);
        orderSortEnumMap.put(OrderSort.TRIAL_DATE.orderSortString, OrderSort.TRIAL_DATE);
        return orderSortEnumMap;
    }

    public String getOrderSortString() {
        return this.orderSortString;
    }
}
