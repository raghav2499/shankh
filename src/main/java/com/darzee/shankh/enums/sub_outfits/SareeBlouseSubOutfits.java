package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SareeBlouseSubOutfits {
    CLASSIC_BLOUSE("classic_blouse", 1),
    PRINCESS_CUT_BLOUSE("princess_cut_blouse", 2),
    HIGH_NECK_BLOUSE("high_neck_blouse", 3),
    HALTER_NECK_BLOUSE("halter_neck_blouse", 4),
    BACKLESS_BLOUSE("backless_blouse", 5),
    JACKET_BLOUSE("jacket_blouse", 6),
    PEPLUM_BLOUSE("peplum_blouse", 7),
    SHEER_BLOUSE("sheer_blouse", 8),
    DESIGNER_BLOUSE("designer_blouse", 9),
    PRINTED_BLOUSE("printed_blouse", 10);

    private String name;
    private Integer ordinal;

    SareeBlouseSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SareeBlouseSubOutfits> sareeBlouseSubOutfitEnumMap = getSareeBlouseSubOutfitEnumMap();

    private static Map<Integer, SareeBlouseSubOutfits> getSareeBlouseSubOutfitEnumMap() {
        Map<Integer, SareeBlouseSubOutfits> sareeBlouseSubOutfitsHashMap = new HashMap<>();
        for (SareeBlouseSubOutfits subOutfit : values()) {
            sareeBlouseSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return sareeBlouseSubOutfitsHashMap;
    }
}
