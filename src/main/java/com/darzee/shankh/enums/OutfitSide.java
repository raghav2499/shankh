package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OutfitSide {

    @JsonProperty("Top")
    TOP("Top"),

    @JsonProperty("Bottom")
    BOTTOM("Bottom");

    private String view;

    OutfitSide(String view) {
        this.view = view;
    }

    static Map<String, OutfitSide> outfitSideMap = getEnumMap();

    public static Map<String, OutfitSide> getEnumMap() {
        Map<String, OutfitSide> outfitSideMap = new HashMap<>();
        outfitSideMap.put(OutfitSide.TOP.view, OutfitSide.TOP);
        outfitSideMap.put(OutfitSide.BOTTOM.view, OutfitSide.BOTTOM);
        return outfitSideMap;
    }

    public String getView() {
        return this.view;
    }
}
