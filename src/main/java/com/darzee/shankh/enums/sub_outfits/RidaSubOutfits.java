package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RidaSubOutfits {
    PLAIN_RIDA("plain_rida", 1),
    EMBROIDERED_RIDA("embroidered_rida", 2),
    PRINTED_RIDA("printed_rida", 3),
    LACE_BORDER_RIDA("lace_border_rida", 4),
    PATCHWORK_RIDA("patchwork_rida", 5),
    TRADITIONAL_RIDA("traditional_rida", 6),
    FORMAL_RIDA("formal_rida", 7),
    EID_SPECIAL_RIDA("eid_special_rida", 8);

    private String name;
    private Integer ordinal;

    RidaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, RidaSubOutfits> ridaSubOutfitEnumMap = getRidaSubOutfitEnumMap();

    private static Map<Integer, RidaSubOutfits> getRidaSubOutfitEnumMap() {
        Map<Integer, RidaSubOutfits> ridaSubOutfitsHashMap = new HashMap<>();
        for (RidaSubOutfits subOutfit : values()) {
            ridaSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return ridaSubOutfitsHashMap;
    }
}
