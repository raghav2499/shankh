package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnderSkirtSubOutfits {
    COTTON_PETTICOATS("cotton_petticoats", 1),
    SILK_PETTICOATS("silk_petticoats", 2),
    SATIN_PETTICOATS("satin_petticoats", 3),
    PRINTED_PETTICOATS("printed_petticoats", 4),
    RUFFLED_PETTICOATS("ruffled_petticoats", 5),
    A_LINE_PETTICOATS("a_line_petticoats", 6),
    DRAWSTRING_PETTICOATS("drawstring_petticoats", 7);

    private String name;
    private Integer ordinal;

    UnderSkirtSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
    private static Map<Integer, UnderSkirtSubOutfits> underSkirtSubOutfitEnumMap = getUnderSkirtSubOutfitEnumMap();

    private static Map<Integer, UnderSkirtSubOutfits> getUnderSkirtSubOutfitEnumMap() {
        Map<Integer, UnderSkirtSubOutfits> underSkirtSubOutfitsHashMap = new HashMap<>();
        for (UnderSkirtSubOutfits subOutfit : values()) {
            underSkirtSubOutfitsHashMap.put(subOutfit.ordinal, subOutfit);
        }
        return underSkirtSubOutfitsHashMap;
    }
}
