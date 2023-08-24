package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PantsSubOutfits {
    FORMAL_TROUSERS("formal_trousers", 1),
    CHINOS("chinos", 2),
    JEANS("jeans", 3),
    CARGOS("cargos", 4),
    TRACKPANTS("trackpants", 5),
    JOGGERS("joggers", 6),
    FORMAL_TROUSERS_WITH_ETHNIC_TOUCH("formal_trousers_with_ethnic_touch", 7);

    private String name;
    private Integer ordinal;

    PantsSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, PantsSubOutfits> pantsSubOutfitEnumMap = getPantsSubOutfitEnumMap();

    private static Map<Integer, PantsSubOutfits> getPantsSubOutfitEnumMap() {
        Map<Integer, PantsSubOutfits> pantsSubOutfitsHashMap = new HashMap<>();
        for (PantsSubOutfits subOutfit : values()) {
            pantsSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return pantsSubOutfitsHashMap;
    }
}