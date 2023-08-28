package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NightGownSubOutfits implements SubOutfitType {
    SHORT_NIGHTIES("short_nighties", 46),
    LONG_NIGHTIES("long_nighties", 47),
    PRINTED_NIGHTIES("printed_nighties", 48),
    SATIN_NIGHTGOWNS("satin_nightgowns", 49),
    MATERNITY_NIGHTGOWNS("maternity_nightgowns", 50);

    private String name;
    private Integer ordinal;

    NightGownSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> nightGownSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> nightGownSubOutfitsHashMap = new HashMap<>();
        for (NightGownSubOutfits subOutfit : values()) {
            nightGownSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return nightGownSubOutfitsHashMap;
    }
}
