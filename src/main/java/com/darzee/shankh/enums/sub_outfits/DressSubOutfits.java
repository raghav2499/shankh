package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DressSubOutfits implements SubOutfitType {
    SHORT_DRESS("short_dress", 1),
    LONG_DRESS("long_dress", 2),
    MAXI_DRESS("maxi_dress", 3);

    private String name;
    private Integer ordinal;

    DressSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> dressSubOutfitEnumMap = getSubOutfitEnumMap();
    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> dressSubOutfitsMap = new HashMap<>();
        for(DressSubOutfits dressSubOutfits : values()) {
            dressSubOutfitsMap.put(dressSubOutfits.ordinal, dressSubOutfits);
        }
        return dressSubOutfitsMap;
    }
}
