package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LadiesSuitSubOutfits implements SubOutfitType {
    SALWAR_KAMEEZ("salwar_kameez", 10),
    ANARKALI_SUIT("anarkali_suit", 11),
    CHURIDAR_SUIT("churidar_suit", 12),
    PATIALA_SUIT("patiala_suit", 13),
    PALAZZO_SUIT("palazzo_suit", 14),
    SHARARA_SUIT("sharara_suit", 15),
    LEHENGA_SUIT("lehenga_suit", 16);

    private String name;
    private Integer ordinal;

    LadiesSuitSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> ladiesSuitSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> ladiesSuitSubOutfitsHashMap = new HashMap<>();
        for (LadiesSuitSubOutfits subOutfit : values()) {
            ladiesSuitSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return ladiesSuitSubOutfitsHashMap;
    }
}
