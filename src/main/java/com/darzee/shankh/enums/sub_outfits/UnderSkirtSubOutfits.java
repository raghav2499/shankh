package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnderSkirtSubOutfits implements SubOutfitType {
    COTTON_PETTICOATS("cotton_petticoats",  85),
    SILK_PETTICOATS("silk_petticoats", 86),
    SATIN_PETTICOATS("satin_petticoats", 87),
    PRINTED_PETTICOATS("printed_petticoats", 88),
    RUFFLED_PETTICOATS("ruffled_petticoats", 89),
    A_LINE_PETTICOATS("a_line_petticoats", 90),
    DRAWSTRING_PETTICOATS("drawstring_petticoats", 91);

    private String name;
    private Integer ordinal;

    UnderSkirtSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
    private static Map<Integer, SubOutfitType> underSkirtSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> underSkirtSubOutfitsHashMap = new HashMap<>();
        for (UnderSkirtSubOutfits subOutfit : values()) {
            underSkirtSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return underSkirtSubOutfitsHashMap;
    }
}
