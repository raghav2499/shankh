package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ShirtSubOutfits {
    FORMAL_SHIRTS("formal_shirts", 1),
    CASUAL_SHIRTS("casual_shirts", 2),
    PRINTED_SHIRTS("printed_shirts", 3),
    STRIPED_SHIRTS("striped_shirts", 4),
    CHECKERED_SHIRTS("checkered_shirts", 5),
    DENIM_SHIRTS("denim_shirts", 6),
    POLO_SHIRTS("polo_shirts", 7),
    KURTA_SHIRTS("kurta_shirts", 8),
    NEHRU_COLLAR_SHIRTS("nehrucollar_shirts", 9);

    private String name;
    private Integer ordinal;

    ShirtSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, ShirtSubOutfits> shirtSubOutfitEnumMap = getShirtSubOutfitEnumMap();

    private static Map<Integer, ShirtSubOutfits> getShirtSubOutfitEnumMap() {
        Map<Integer, ShirtSubOutfits> shirtSubOutfitsHashMap = new HashMap<>();
        for (ShirtSubOutfits subOutfit : values()) {
            shirtSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return shirtSubOutfitsHashMap;
    }
}
