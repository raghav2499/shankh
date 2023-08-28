package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ImageEntityType {

    @JsonProperty("boutique")
    BOUTIQUE("boutique"),
    @JsonProperty("order")
    ORDER("order"),

    @JsonProperty("customer")
    CUSTOMER("customer"),

    @JsonProperty("tailor")
    TAILOR("tailor"),

    @JsonProperty("portfolio_cover")
    PORTFOLIO_COVER("portfolio_cover"),

    @JsonProperty("portfolio_profile")
    PORTFOLIO_PROFILE("portfolio_profile"),

    @JsonProperty("portfolio_outfit")
    PORTFOLIO_OUTFIT("portfolio_outfit");

    private String entityType;

    ImageEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
    }
}
