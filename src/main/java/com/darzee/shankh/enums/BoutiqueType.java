package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BoutiqueType {
    @JsonProperty("gents")
    GENTS("gents", 1),
    @JsonProperty("ladies")
    LADIES("ladies", 2),
    @JsonProperty("both")
    BOTH("both", 3);
    private String boutiqueType;

    private Integer ordinal;

    static Map<String, BoutiqueType> boutiqueTypeEnumMap = getEnumMap();

    BoutiqueType(String boutiqueType, Integer ordinal) {
        this.boutiqueType = boutiqueType;
        this.ordinal = ordinal;
    }

    public static Map<String, BoutiqueType> getEnumMap() {
        Map<String, BoutiqueType> boutiqueTypeEnumMap = new HashMap<>();
        boutiqueTypeEnumMap.put("gents", BoutiqueType.GENTS);
        boutiqueTypeEnumMap.put("ladies", BoutiqueType.LADIES);
        boutiqueTypeEnumMap.put("both", BoutiqueType.BOTH);
        return boutiqueTypeEnumMap;
    }

    public static Map<Integer, BoutiqueType> getOrdinalEnumMap() {
        Map<Integer, BoutiqueType> boutiqueTypeEnumMap = new HashMap<>();
        boutiqueTypeEnumMap.put(BoutiqueType.GENTS.ordinal, BoutiqueType.GENTS);
        boutiqueTypeEnumMap.put(BoutiqueType.LADIES.ordinal, BoutiqueType.LADIES);
        boutiqueTypeEnumMap.put(BoutiqueType.BOTH.ordinal, BoutiqueType.BOTH);
        return boutiqueTypeEnumMap;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }

}
