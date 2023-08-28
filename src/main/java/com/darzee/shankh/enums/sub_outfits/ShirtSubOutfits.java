package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ShirtSubOutfits implements SubOutfitType {
    FORMAL_SHIRTS("formal_shirts", 76),
    CASUAL_SHIRTS("casual_shirts", 77),
    PRINTED_SHIRTS("printed_shirts", 78),
    STRIPED_SHIRTS("striped_shirts", 79),
    CHECKERED_SHIRTS("checkered_shirts", 80),
    DENIM_SHIRTS("denim_shirts", 81),
    POLO_SHIRTS("polo_shirts", 82),
    KURTA_SHIRTS("kurta_shirts", 83),
    NEHRU_COLLAR_SHIRTS("nehrucollar_shirts", 84);

    private String name;
    private Integer ordinal;

    ShirtSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> shirtSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> shirtSubOutfitsHashMap = new HashMap<>();
        for (ShirtSubOutfits subOutfit : values()) {
            shirtSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return shirtSubOutfitsHashMap;
    }
}
