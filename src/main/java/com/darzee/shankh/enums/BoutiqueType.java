package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BoutiqueType {
    @JsonProperty("gents")
    GENTS("gents"),
    @JsonProperty("ladies")
    LADIES("ladies"),
    @JsonProperty("both")
    BOTH("both");
    private String boutiqueType;

    static Map<String, BoutiqueType> boutiqueTypeEnumMap = getEnumMap();

    BoutiqueType(String boutiqueType) {
        this.boutiqueType = boutiqueType;
    }

    public static Map<String, BoutiqueType> getEnumMap() {
        Map<String, BoutiqueType> boutiqueTypeEnumMap = new HashMap<>();
        boutiqueTypeEnumMap.put("gents", BoutiqueType.GENTS);
        boutiqueTypeEnumMap.put("ladies", BoutiqueType.LADIES);
        boutiqueTypeEnumMap.put("both", BoutiqueType.BOTH);
        return boutiqueTypeEnumMap;
    }

}
