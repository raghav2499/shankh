package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OutfitType {

    @JsonProperty("kurta_pyjama")
    KURTA_PYJAMA("kurta_pyjama", 1, "Kurta Pajama", Arrays.asList(Gender.MALE)),
    @JsonProperty("dress")
    DRESS("dress", 2, "Dress", Arrays.asList(Gender.FEMALE)),
    @JsonProperty("saree_blouse")
    SAREE_BLOUSE("saree_blouse", 3, "Saree Blouse", Arrays.asList(Gender.FEMALE)),
    @JsonProperty("mens_suit")
    MENS_SUIT("mens_suit", 4, "Men's Suit", Arrays.asList(Gender.MALE)),
    @JsonProperty("pants")
    PANTS("pants", 5, "Pants", Arrays.asList(Gender.MALE)),
    @JsonProperty("night_gown")
    EVENING_GOWN("night_gown", 6, "Night Gown", Arrays.asList(Gender.FEMALE)),
    @JsonProperty("ladies_suit")
    LADIES_SUIT("ladies_suit", 7, "Ladies Suit", Arrays.asList(Gender.FEMALE)),
    @JsonProperty("shirt")
    SHIRT("shirt", 8, "Shirt", Arrays.asList(Gender.MALE)),
    @JsonProperty("under_skirt")
    UNDER_SKIRT("under_skirt", 9, "Under Skirt", Arrays.asList(Gender.FEMALE)),

    @JsonProperty("nehru_jacket")
    NEHRU_JACKET("nehru_jacket", 10, "Nehru Jacket", Arrays.asList(Gender.MALE)),

    @JsonProperty("rida")
    RIDA("rida", 11, "Rida", Arrays.asList(Gender.FEMALE)),

    @JsonProperty("waist_coat")
    WAIST_COAT("waist_coat", 12, "Waist Coat", Arrays.asList(Gender.MALE)),

    @JsonProperty("lehenga")
    LEHENGA("lehenga", 13, "Lehenga", Arrays.asList(Gender.FEMALE));
    private String name;
    private Integer ordinal;

    private String displayString;

    private List<Gender> gender;

    OutfitType(String name, Integer ordinal, String displayString, List<Gender> genderList) {
        this.name = name;
        this.ordinal = ordinal;
        this.displayString = displayString;
        this.gender = genderList;
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

    public List<Gender> getGenderList() {
        return this.gender;
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
        outfitEnumMap.put(NEHRU_JACKET.name, NEHRU_JACKET);
        outfitEnumMap.put(RIDA.name, RIDA);
        outfitEnumMap.put(WAIST_COAT.name, WAIST_COAT);
        outfitEnumMap.put(LEHENGA.name, LEHENGA);
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
        outfitOrdinalEnumMap.put(NEHRU_JACKET.ordinal, NEHRU_JACKET);
        outfitOrdinalEnumMap.put(RIDA.ordinal, RIDA);
        outfitOrdinalEnumMap.put(WAIST_COAT.ordinal, WAIST_COAT);
        outfitOrdinalEnumMap.put(LEHENGA.ordinal, LEHENGA);
        return outfitOrdinalEnumMap;
    }

}
