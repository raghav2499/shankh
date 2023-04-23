package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderType {

    @JsonProperty("stitching")
    STITCHING("stitching"),
    @JsonProperty("alteration")
    ALTERATION("alteration");
    private String orderType;

    OrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getName() {
        return this.orderType;
    }
}
