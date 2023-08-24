package com.darzee.shankh.enums.sub_outfits;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KurtaPyjamaSubOutfits {
    PATHANI_SUIT("pathani_suit", 1),
    SHERWANI("sherwani", 2),
    ACHKAN("achkan", 3),
    JODHPURI_SUIT("jodhpuri_suit", 4),
    INDO_WESTERN_KURTA_PYJAMA("indo_western_kurta_pyjama", 5),
    DESIGNER_KURTA_PYJAMA("designer_kurta_pyjama", 6);

    private String name;
    private Integer ordinal;

    KurtaPyjamaSubOutfits(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    private static Map<Integer, KurtaPyjamaSubOutfits> kurtaPyjamaSubOutfitEnumMap = getKurtaPyjamaSubOutfitEnumMap();
    private static Map<Integer, KurtaPyjamaSubOutfits> getKurtaPyjamaSubOutfitEnumMap() {
        Map<Integer, KurtaPyjamaSubOutfits> kurtaPyjamaSubOutfitsHashMap = new HashMap<>();
        for(KurtaPyjamaSubOutfits kurtaPyjamaSubOutfits : values()) {
            kurtaPyjamaSubOutfitsHashMap.put(kurtaPyjamaSubOutfits.ordinal, kurtaPyjamaSubOutfits);
        }
        return kurtaPyjamaSubOutfitsHashMap;
    }
}
