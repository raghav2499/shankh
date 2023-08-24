package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NightGownSubOutfits {
    SHORT_NIGHTIES("short_nighties", 1),
    LONG_NIGHTIES("long_nighties", 2),
    PRINTED_NIGHTIES("printed_nighties", 3),
    SATIN_NIGHTGOWNS("satin_nightgowns", 4),
    MATERNITY_NIGHTGOWNS("maternity_nightgowns", 5);

    private String name;
    private Integer ordinal;

    NightGownSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, NightGownSubOutfits> nightGownSubOutfitEnumMap = getNightGownSubOutfitEnumMap();

    private static Map<Integer, NightGownSubOutfits> getNightGownSubOutfitEnumMap() {
        Map<Integer, NightGownSubOutfits> nightGownSubOutfitsHashMap = new HashMap<>();
        for (NightGownSubOutfits subOutfit : values()) {
            nightGownSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return nightGownSubOutfitsHashMap;
    }
}
