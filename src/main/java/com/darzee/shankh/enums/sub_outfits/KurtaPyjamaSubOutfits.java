package com.darzee.shankh.enums.sub_outfits;

import com.darzee.shankh.enums.SubOutfitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KurtaPyjamaSubOutfits implements SubOutfitType {
    PATHANI_SUIT("pathani_suit", 4),
    SHERWANI("sherwani", 5),
    ACHKAN("achkan", 6),
    JODHPURI_SUIT("jodhpuri_suit", 7),
    INDO_WESTERN_KURTA_PYJAMA("indo_western_kurta_pyjama", 8),
    DESIGNER_KURTA_PYJAMA("designer_kurta_pyjama", 9);

    private String name;
    private Integer ordinal;

    KurtaPyjamaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, SubOutfitType> kurtaPyjamaSubOutfitEnumMap = getSubOutfitEnumMap();

    public static Map<Integer, SubOutfitType> getSubOutfitEnumMap() {
        Map<Integer, SubOutfitType> kurtaPyjamaSubOutfitsHashMap = new HashMap<>();
        for (KurtaPyjamaSubOutfits kurtaPyjamaSubOutfits : values()) {
            kurtaPyjamaSubOutfitsHashMap.put(kurtaPyjamaSubOutfits.ordinal, kurtaPyjamaSubOutfits);
        }
        return kurtaPyjamaSubOutfitsHashMap;
    }
}
