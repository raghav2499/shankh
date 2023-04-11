package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ImageEntityType {

    @JsonProperty("boutique")
    BOUTIQUE("boutique"),
    @JsonProperty("order")
    ORDER("order");
    private String entityType;

    ImageEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
    }
}
