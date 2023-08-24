package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NehruJacketSubOutfits {
    CLASSIC_NEHRU_JACKET("classic_nehru_jacket", 1),
    EMBROIDERED_NEHRU_JACKET("embroidered_nehru_jacket", 2),
    PRINTED_NEHRU_JACKET("printed_nehru_jacket", 3),
    VELVET_NEHRU_JACKET("velvet_nehru_jacket", 4),
    LINEN_NEHRU_JACKET("linen_nehru_jacket", 5),
    SILK_NEHRU_JACKET("silk_nehru_jacket", 6),
    TEXTURED_NEHRU_JACKET("textured_nehru_jacket", 7);

    private String name;
    private Integer ordinal;

    NehruJacketSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, NehruJacketSubOutfits> nehruJacketSubOutfitEnumMap = getNehruJacketSubOutfitEnumMap();

    private static Map<Integer, NehruJacketSubOutfits> getNehruJacketSubOutfitEnumMap() {
        Map<Integer, NehruJacketSubOutfits> nehruJacketSubOutfitsHashMap = new HashMap<>();
        for (NehruJacketSubOutfits subOutfit : values()) {
            nehruJacketSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return nehruJacketSubOutfitsHashMap;
    }
}
