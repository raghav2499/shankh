package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WaistcoatSubOutfits {
    SINGLE_BREASTED_WAISTCOAT("single_breasted_waistcoat", 1),
    DOUBLE_BREASTED_WAISTCOAT("double_breasted_waistcoat", 2),
    SHAWL_COLLAR_WAISTCOAT("shawl_collar_waistcoat", 3),
    NOTCH_COLLAR_WAISTCOAT("notch_collar_waistcoat", 4),
    MANDARIN_COLLAR_WAISTCOAT("mandarin_collar_waistcoat", 5),
    FULL_BACK_WAISTCOAT("full_back_waistcoat", 6),
    ADJUSTABLE_BACK_WAISTCOAT("adjustable_back_waistcoat", 7),
    PATTERNED_WAISTCOAT("patterned_waistcoat", 8),
    SILK_WAISTCOAT("silk_waistcoat", 9),
    TEXTURED_WAISTCOAT("textured_waistcoat", 10);

    private String name;
    private Integer ordinal;

    WaistcoatSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, WaistcoatSubOutfits> waistCoatSubOutfitEnumMap = getWaistCoatSubOutfitEnumMap();

    private static Map<Integer, WaistcoatSubOutfits> getWaistCoatSubOutfitEnumMap() {
        Map<Integer, WaistcoatSubOutfits> waistCoatSubOutfitsHashMap = new HashMap<>();
        for (WaistcoatSubOutfits subOutfit : values()) {
            waistCoatSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return waistCoatSubOutfitsHashMap;
    }
}
