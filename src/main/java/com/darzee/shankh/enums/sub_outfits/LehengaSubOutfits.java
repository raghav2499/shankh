package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LehengaSubOutfits {
    A_LINE_LEHENGA("a_line_lehenga", 1),
    CIRCULAR_LEHENGA("circular_lehenga", 2),
    MERMAID_LEHENGA("mermaid_lehenga", 3),
    PANELLED_LEHENGA("panelled_lehenga", 4),
    JACKET_LEHENGA("jacket_lehenga", 5),
    SHARARA_LEHENGA("sharara_lehenga", 6),
    TRAIL_LEHENGA("trail_lehenga", 7),
    LEHENGA_SAREE("lehenga_saree", 8),
    FLARED_LEHENGA("flared_lehenga", 9),
    RUFFLED_LEHENGA("ruffled_lehenga", 10),
    STRAIGHT_CUT_LEHENGA("straight_cut_lehenga", 11),
    HALF_SAREE_LEHENGA("half_saree_lehenga", 12),
    LEHENGA_WITH_CAPE("lehenga_with_cape", 13),
    ASYMMETRIC_LEHENGA("asymmetric_lehenga", 14),
    TIERED_LEHENGA("tiered_lehenga", 15),
    INDOWESTERN_LEHENGA("indowestern_lehenga", 16);

    private String name;
    private Integer ordinal;

    LehengaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, LehengaSubOutfits> lehengaSubOutfitEnumMap = getLehengaSubOutfitEnumMap();

    private static Map<Integer, LehengaSubOutfits> getLehengaSubOutfitEnumMap() {
        Map<Integer, LehengaSubOutfits> lehengaSubOutfitsHashMap = new HashMap<>();
        for (LehengaSubOutfits subOutfit : values()) {
            lehengaSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return lehengaSubOutfitsHashMap;
    }
}
