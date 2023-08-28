package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NehruJacketSubOutfits implements SubOutfitType {
    CLASSIC_NEHRU_JACKET("classic_nehru_jacket", 39),
    EMBROIDERED_NEHRU_JACKET("embroidered_nehru_jacket", 40),
    PRINTED_NEHRU_JACKET("printed_nehru_jacket", 41),
    VELVET_NEHRU_JACKET("velvet_nehru_jacket", 42),
    LINEN_NEHRU_JACKET("linen_nehru_jacket", 43),
    SILK_NEHRU_JACKET("silk_nehru_jacket", 44),
    TEXTURED_NEHRU_JACKET("textured_nehru_jacket", 45);

    private String name;
    private Integer ordinal;

    NehruJacketSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> nehruJacketSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> nehruJacketSubOutfitsHashMap = new HashMap<>();
        for (NehruJacketSubOutfits subOutfit : values()) {
            nehruJacketSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return nehruJacketSubOutfitsHashMap;
    }
}
