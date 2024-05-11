package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileEntityType {

    @JsonProperty("boutique")
    BOUTIQUE("boutique"),
    @JsonProperty("order_item")
    ORDER_ITEM("order_item"),

    @JsonProperty("customer")
    CUSTOMER("customer"),

    @JsonProperty("tailor")
    TAILOR("tailor"),

    @JsonProperty("portfolio_cover")
    PORTFOLIO_COVER("portfolio_cover"),

    @JsonProperty("portfolio_profile")
    PORTFOLIO_PROFILE("portfolio_profile"),

    @JsonProperty("portfolio_outfit")
    PORTFOLIO_OUTFIT("portfolio_outfit"),

    @JsonProperty("audio")
    AUDIO("audio"),

    @JsonProperty("measurement_revision")
    MEASUREMENT_REVISION("measurement_revision");

    private String entityType;

    FileEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
    }
}
