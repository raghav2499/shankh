package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OutfitType {

    @JsonProperty("kurta_pyjama")
    KURTA_PYJAMA("kurta_pyjama"),
    @JsonProperty("dress")
    DRESS("dress"),
    @JsonProperty("saree_blouse")
    SAREE_BLOUSE("saree_blouse"),
    @JsonProperty("mens_suit")
    MENS_SUIT("mens_suit"),
    @JsonProperty("pants")
    PANTS("pants"),
    @JsonProperty("night_gown")
    EVENING_GOWN("Night Gown"),
    @JsonProperty("salwar_kameez")
    LADIES_SUIT("salwar_kameez"),
    @JsonProperty("shirt")
    SHIRT("shirt"),
    @JsonProperty("under_skirt")
    UNDER_SKIRT("under_skirt");
    private String name;

    OutfitType(String name) {
        this.name = name;
    }


    public static Map<String, OutfitType> outfitEnumMap  = getOutfitEnumMap();

    public static Map<String, OutfitType> getOutfitEnumMap() {
        Map<String, OutfitType> outfitEnumMap = new HashMap<>();
        outfitEnumMap.put(KURTA_PYJAMA.name, KURTA_PYJAMA);
        outfitEnumMap.put(DRESS.name, DRESS);
        outfitEnumMap.put(SAREE_BLOUSE.name, SAREE_BLOUSE);
        outfitEnumMap.put(MENS_SUIT.name, MENS_SUIT);
        outfitEnumMap.put(PANTS.name, PANTS);
        outfitEnumMap.put(EVENING_GOWN.name, EVENING_GOWN);
        outfitEnumMap.put(LADIES_SUIT.name, LADIES_SUIT);
        outfitEnumMap.put(SHIRT.name, SHIRT);
        outfitEnumMap.put(UNDER_SKIRT.name, UNDER_SKIRT);
        return outfitEnumMap;
    }

}
