package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WaistcoatSubOutfits implements SubOutfitType {
    SINGLE_BREASTED_WAISTCOAT("single_breasted_waistcoat", 92),
    DOUBLE_BREASTED_WAISTCOAT("double_breasted_waistcoat", 93),
    SHAWL_COLLAR_WAISTCOAT("shawl_collar_waistcoat", 94),
    NOTCH_COLLAR_WAISTCOAT("notch_collar_waistcoat", 95),
    MANDARIN_COLLAR_WAISTCOAT("mandarin_collar_waistcoat", 96),
    FULL_BACK_WAISTCOAT("full_back_waistcoat", 97),
    ADJUSTABLE_BACK_WAISTCOAT("adjustable_back_waistcoat", 98),
    PATTERNED_WAISTCOAT("patterned_waistcoat", 99),
    SILK_WAISTCOAT("silk_waistcoat", 100),
    TEXTURED_WAISTCOAT("textured_waistcoat", 101);

    private String name;
    private Integer ordinal;

    WaistcoatSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> waistCoatSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> waistCoatSubOutfitsHashMap = new HashMap<>();
        for (WaistcoatSubOutfits subOutfit : values()) {
            waistCoatSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return waistCoatSubOutfitsHashMap;
    }
}
