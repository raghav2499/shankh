package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SareeBlouseSubOutfits implements SubOutfitType {
    CLASSIC_BLOUSE("classic_blouse", 66),
    PRINCESS_CUT_BLOUSE("princess_cut_blouse", 67),
    HIGH_NECK_BLOUSE("high_neck_blouse", 68),
    HALTER_NECK_BLOUSE("halter_neck_blouse", 69),
    BACKLESS_BLOUSE("backless_blouse", 70),
    JACKET_BLOUSE("jacket_blouse", 71),
    PEPLUM_BLOUSE("peplum_blouse", 72),
    SHEER_BLOUSE("sheer_blouse", 73),
    DESIGNER_BLOUSE("designer_blouse", 74),
    PRINTED_BLOUSE("printed_blouse", 75);

    private String name;
    private Integer ordinal;

    SareeBlouseSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> sareeBlouseSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> sareeBlouseSubOutfitsHashMap = new HashMap<>();
        for (SareeBlouseSubOutfits subOutfit : values()) {
            sareeBlouseSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return sareeBlouseSubOutfitsHashMap;
    }
}
