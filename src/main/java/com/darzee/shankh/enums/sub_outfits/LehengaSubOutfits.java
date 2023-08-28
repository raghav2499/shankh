package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LehengaSubOutfits implements SubOutfitType {
    A_LINE_LEHENGA("a_line_lehenga", 17),
    CIRCULAR_LEHENGA("circular_lehenga", 18),
    MERMAID_LEHENGA("mermaid_lehenga", 19),
    PANELLED_LEHENGA("panelled_lehenga", 20),
    JACKET_LEHENGA("jacket_lehenga", 21),
    SHARARA_LEHENGA("sharara_lehenga", 22),
    TRAIL_LEHENGA("trail_lehenga", 23),
    LEHENGA_SAREE("lehenga_saree", 24),
    FLARED_LEHENGA("flared_lehenga", 25),
    RUFFLED_LEHENGA("ruffled_lehenga", 26),
    STRAIGHT_CUT_LEHENGA("straight_cut_lehenga", 27),
    HALF_SAREE_LEHENGA("half_saree_lehenga", 28),
    LEHENGA_WITH_CAPE("lehenga_with_cape", 29),
    ASYMMETRIC_LEHENGA("asymmetric_lehenga", 30),
    TIERED_LEHENGA("tiered_lehenga", 31),
    INDOWESTERN_LEHENGA("indowestern_lehenga", 32);

    private String name;
    private Integer ordinal;

    LehengaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> lehengaSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> lehengaSubOutfitsHashMap = new HashMap<>();
        for (LehengaSubOutfits subOutfit : values()) {
            lehengaSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return lehengaSubOutfitsHashMap;
    }
}
