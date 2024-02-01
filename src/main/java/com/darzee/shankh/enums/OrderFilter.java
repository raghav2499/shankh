package com.darzee.shankh.enums;

import java.util.Arrays;

public enum OrderFilter {

    STATUS("status"),
    BOUTIQUE_ID("boutiqueId"),
    PRIORITY_ORDERS_ONLY("priorityOrdersOnly"),

    CUSTOMER_ID("customerId"),

    DELIVERY_DATE_FROM("deliveryDateFrom"),

    DELIVERY_DATE_TILL("deliveryDateTill"),

    ORDER_ID("order_id");

    OrderFilter(String filterName) {
        this.filterName = filterName;
    }

    public static OrderFilter getFilter(String filterName) {
        return Arrays.stream(OrderFilter.values())
                .filter(filter -> filter.getFilterName().equals(filterName))
                .findFirst()
                .orElse(null);
    }
    private String filterName;

    public String getFilterName() {
        return this.filterName;
    }
}

