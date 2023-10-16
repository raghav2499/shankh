package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorEnum {

    RED("Red", 1),
    ROYAL_BLUE("Royal Blue", 2),
    LIGHT_YELLOW("Light Yellow", 3),
    GREEN("Green", 4),
    ORANGE("Orange", 5),
    PURPLE("Purple", 6),
    BABY_PINK("Baby Pink", 7),
    BROWN("Brown", 8),
    BLACK("Black", 9),
    WHITE("White", 10),
    LIGHT_GREY("Light Grey", 11),
    BEIGE("Beige", 12),
    NAVY_BLUE("Navy Blue", 13),
    TORQUOISE("Torquoise", 14),
    MAROON("Maroon", 15),
    YELLOW("Yellow", 16),
    GREY("Grey", 17),
    CREAM("Cream", 18),
    OLIVE("Olive", 19),
    TEAL("Teal", 20);

    private String name;
    private Integer ordinal;

    ColorEnum(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public static Map<Integer, ColorEnum> outfitOrdinalEnumMap = getColorOrdinalEnumMap();

    public static Map<Integer, ColorEnum> getColorOrdinalEnumMap() {
        Map<Integer, ColorEnum> colorEnumOrdinalMap = new HashMap<>();
        for (ColorEnum color : ColorEnum.values()) {
            colorEnumOrdinalMap.put(color.ordinal, color);
        }
        return colorEnumOrdinalMap;
    }

    public String getName() {
        return name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }
}
