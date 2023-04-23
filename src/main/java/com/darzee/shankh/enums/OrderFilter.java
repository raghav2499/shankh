package com.darzee.shankh.enums;

import java.util.Arrays;

public enum OrderFilter {

    STATUS("status"),
    BOUTIQUE_ID("boutiqueId"),
    PRIORITY_ORDERS_ONLY("priorityOrdersOnly");

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

