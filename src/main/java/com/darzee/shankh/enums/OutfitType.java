package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static com.darzee.shankh.constants.Constants.OutfitType.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OutfitType {

    @JsonProperty("kurta_pyjama")
    KURTA_PYJAMA("kurta_pyjama", 1, "Kurta Pajama", OUTFIT_TYPE_KURTA_PYJAMA_LINK),
    @JsonProperty("dress")
    DRESS("dress", 2, "Dress", OUTFIT_TYPE_DRESS_LINK),
    @JsonProperty("saree_blouse")
    SAREE_BLOUSE("saree_blouse", 3, "Saree Blouse", OUTFIT_TYPE_SAREE_BLOUSE_LINK),
    @JsonProperty("mens_suit")
    MENS_SUIT("mens_suit", 4, "Men's Suit", OUTFIT_TYPE_MENS_SUIT_LINK),
    @JsonProperty("pants")
    PANTS("pants", 5, "Pants", OUTFIT_TYPE_PANT_LINK),
    @JsonProperty("night_gown")
    EVENING_GOWN("night_gown", 6, "Night Gown", OUTFIT_TYPE_NIGHT_GOWN_LINK),
    @JsonProperty("ladies_suit")
    LADIES_SUIT("ladies_suit", 7, "Ladies Suit", OUTFIT_TYPE_NIGHT_GOWN_LINK),
    @JsonProperty("shirt")
    SHIRT("shirt", 8, "Shirt", OUTFIT_TYPE_SHIRT_LINK),
    @JsonProperty("under_skirt")
    UNDER_SKIRT("under_skirt", 9, "Under Skirt", OUTFIT_TYPE_UNDER_SKIRT_LINK);
    private String name;
    private Integer ordinal;

    private String displayString;

    private String imageLink;

    OutfitType(String name, Integer ordinal, String displayString, String imageLink) {
        this.name = name;
        this.ordinal = ordinal;
        this.displayString = displayString;
        this.imageLink = imageLink;
    }

    public String getName() {
        return this.name;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }

    public String getDisplayString() {
        return this.displayString;
    }

    public String getImageLink() {
        return this.imageLink;
    }


    public static Map<String, OutfitType> outfitEnumMap = getOutfitEnumMap();
    public static Map<Integer, OutfitType> outfitOrdinalEnumMap = getOutfitOrdinalEnumMap();

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

    public static Map<Integer, OutfitType> getOutfitOrdinalEnumMap() {
        Map<Integer, OutfitType> outfitOrdinalEnumMap = new HashMap<>();
        outfitOrdinalEnumMap.put(KURTA_PYJAMA.ordinal, KURTA_PYJAMA);
        outfitOrdinalEnumMap.put(DRESS.ordinal, DRESS);
        outfitOrdinalEnumMap.put(SAREE_BLOUSE.ordinal, SAREE_BLOUSE);
        outfitOrdinalEnumMap.put(MENS_SUIT.ordinal, MENS_SUIT);
        outfitOrdinalEnumMap.put(PANTS.ordinal, PANTS);
        outfitOrdinalEnumMap.put(EVENING_GOWN.ordinal, EVENING_GOWN);
        outfitOrdinalEnumMap.put(LADIES_SUIT.ordinal, LADIES_SUIT);
        outfitOrdinalEnumMap.put(SHIRT.ordinal, SHIRT);
        outfitOrdinalEnumMap.put(UNDER_SKIRT.ordinal, UNDER_SKIRT);
        return outfitOrdinalEnumMap;
    }

}
