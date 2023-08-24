package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LadiesSuitSubOutfits {
    SALWAR_KAMEEZ("salwar_kameez", 1),
    ANARKALI_SUIT("anarkali_suit", 2),
    CHURIDAR_SUIT("churidar_suit", 3),
    PATIALA_SUIT("patiala_suit", 4),
    PALAZZO_SUIT("palazzo_suit", 5),
    SHARARA_SUIT("sharara_suit", 6),
    LEHENGA_SUIT("lehenga_suit", 7);

    private String name;
    private Integer ordinal;

    LadiesSuitSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, LadiesSuitSubOutfits> ladiesSuitSubOutfitEnumMap = getLadiesSuitSubOutfitEnumMap();

    private static Map<Integer, LadiesSuitSubOutfits> getLadiesSuitSubOutfitEnumMap() {
        Map<Integer, LadiesSuitSubOutfits> ladiesSuitSubOutfitsHashMap = new HashMap<>();
        for (LadiesSuitSubOutfits subOutfit : values()) {
            ladiesSuitSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return ladiesSuitSubOutfitsHashMap;
    }
}
