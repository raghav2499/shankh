package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RidaSubOutfits implements SubOutfitType{
    PLAIN_RIDA("plain_rida", 58),
    EMBROIDERED_RIDA("embroidered_rida", 59),
    PRINTED_RIDA("printed_rida", 60),
    LACE_BORDER_RIDA("lace_border_rida", 61),
    PATCHWORK_RIDA("patchwork_rida", 62),
    TRADITIONAL_RIDA("traditional_rida", 63),
    FORMAL_RIDA("formal_rida", 64),
    EID_SPECIAL_RIDA("eid_special_rida", 65);

    private String name;
    private Integer ordinal;

    RidaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> ridaSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> ridaSubOutfitsHashMap = new HashMap<>();
        for (RidaSubOutfits subOutfit : values()) {
            ridaSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return ridaSubOutfitsHashMap;
    }
}
