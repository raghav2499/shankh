package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MensSuitSubOutfits implements SubOutfitType {

    FORMAL_BUSINESS_SUIT("formal_business_suit", 33),
    WEDDING_SUIT("wedding_suit", 34),
    BANDHGALA_SUIT("bandhgala_suit", 35),
    JODHPURI_SUIT("jodhpuri_suit", 36),
    INDO_WESTERN_SUIT("indo_western_suit", 37),
    TUXEDO("tuxedo", 38);

    private String name;
    private Integer ordinal;

    MensSuitSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> mensSuitSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> mensSuitSubOutfitsHashMap = new HashMap<>();
        for (MensSuitSubOutfits subOutfit : values()) {
            mensSuitSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return mensSuitSubOutfitsHashMap;
    }
}
