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
    LEHENGA("lehenga", 13, "Lehenga", Arrays.asList(Gender.FEMALE)),

    @JsonProperty("sharara")
    SHARARA("sharara", 14, "Sharara", Arrays.asList(Gender.FEMALE)),

    @JsonProperty("sherwani")
    SHERWANI("sherwani", 15, "Sherwani", Arrays.asList(Gender.MALE)),

    @JsonProperty("indo_western")
    INDO_WESTERN("indo_western", 16, "Indo Western", Arrays.asList(Gender.MALE));
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
        for(OutfitType outfitType : OutfitType.values()) {
            outfitEnumMap.put(outfitType.name, outfitType);
        }
        return outfitEnumMap;
    }

    public static Map<Integer, OutfitType> getOutfitOrdinalEnumMap() {
        Map<Integer, OutfitType> outfitOrdinalEnumMap = new HashMap<>();
        for(OutfitType outfitType : OutfitType.values()) {
            outfitOrdinalEnumMap.put(outfitType.ordinal, outfitType);
        }
        return outfitOrdinalEnumMap;
    }

}
