package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MensSuitSubOutfits {

    FORMAL_BUSINESS_SUIT("formal_business_suit", 1),
    WEDDING_SUIT("wedding_suit", 2),
    BANDHGALA_SUIT("bandhgala_suit", 3),
    JODHPURI_SUIT("jodhpuri_suit", 4),
    INDO_WESTERN_SUIT("indo_western_suit", 5),
    TUXEDO("tuxedo", 6);

    private String name;
    private Integer ordinal;

    MensSuitSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, MensSuitSubOutfits> mensSuitSubOutfitEnumMap = getMensSuitSubOutfitEnumMap();

    private static Map<Integer, MensSuitSubOutfits> getMensSuitSubOutfitEnumMap() {
        Map<Integer, MensSuitSubOutfits> mensSuitSubOutfitsHashMap = new HashMap<>();
        for (MensSuitSubOutfits subOutfit : values()) {
            mensSuitSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return mensSuitSubOutfitsHashMap;
    }
}
