package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PantsSubOutfits implements SubOutfitType {
    FORMAL_TROUSERS("formal_trousers", 51),
    CHINOS("chinos", 52),
    JEANS("jeans", 53),
    CARGOS("cargos", 54),
    TRACKPANTS("trackpants", 55),
    JOGGERS("joggers", 56),
    FORMAL_TROUSERS_WITH_ETHNIC_TOUCH("formal_trousers_with_ethnic_touch", 57);

    private String name;
    private Integer ordinal;

    PantsSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> pantsSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> pantsSubOutfitsHashMap = new HashMap<>();
        for (PantsSubOutfits subOutfit : values()) {
            pantsSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return pantsSubOutfitsHashMap;
    }
}