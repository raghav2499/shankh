package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderType {

    @JsonProperty("stitching") STITCHING("stitching", "Stitching"), @JsonProperty("alteration") ALTERATION("alteration", "Alteration");
    private String orderType;

    private String displayString;

    OrderType(String orderType, String displayString) {
        this.orderType = orderType;
        this.displayString = displayString;
    }

    public String getName() {
        return this.orderType;
    }
    public String getDisplayName() {
        return this.displayString;
    }
}
