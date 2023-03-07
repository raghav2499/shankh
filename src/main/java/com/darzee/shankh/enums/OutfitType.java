package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OutfitType {

    @JsonProperty("Kurta Pyjama")
    KURTA_PYJAMA("Kurta Pyjama"),
    @JsonProperty("Dress")
    DRESS("Dress"),
    @JsonProperty("Saree Blouse")
    SAREE_BLOUSE("Saree Blouse"),
    @JsonProperty("Men's Suit")
    MENS_SUIT("Men's Suit"),
    @JsonProperty("Pants")
    PANTS("Pants"),
    @JsonProperty("Night Gown")
    EVENING_GOWN("Night Gown"),
    @JsonProperty("Salwar Kameez")
    LADIES_SUIT("Salwar Kameez"),
    @JsonProperty("Shirt")
    SHIRT("Shirt"),
    @JsonProperty("Under Skirt")
    UNDER_SKIRT("Under Skirt");
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
