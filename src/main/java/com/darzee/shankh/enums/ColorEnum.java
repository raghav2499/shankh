package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorEnum {

    RED("red", 1),
    ROYAL_BLUE("royal_blue", 2),
    LIGHT_YELLOW("light_yellow", 3),
    GREEN("green", 4),
    ORANGE("orange", 5),
    PURPLE("purple", 6),
    BABY_PINK("baby_pink", 7),
    BROWN("brown", 8),
    BLACK("black", 9),
    WHITE("white", 10),
    LIGHT_GREY("light_grey", 11),
    BEIGE("beige", 12),
    NAVY_BLUE("navy_blue", 13),
    TORQUOISE("torquoise", 14),
    MAROON("maroon", 15),
    YELLOW("yellow", 16),
    GREY("grey", 17),
    CREAM("cream", 18),
    OLIVE("olive", 19),
    TEAL("teal", 20);

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
}
