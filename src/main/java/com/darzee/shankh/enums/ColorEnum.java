package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorEnum {

    RED("Red", "DF1A1A", 1),
    ROYAL_BLUE("Royal Blue", "3E2FE8", 2),
    LIGHT_YELLOW("Light Yellow", "F0DF45", 3),
    GREEN("Green", "3AD15C", 4),
    ORANGE("Orange", "FDBA0E", 5),
    PURPLE("Purple", "800080", 6),
    BABY_PINK("Baby Pink", "F998D2", 7),
    BROWN("Brown", "964B00", 8),
    BLACK("Black", "000000", 9),
    WHITE("White", "FFFFFF", 10),
    LIGHT_GREY("Light Grey", "D9D9D9", 11),
    BEIGE("Beige", "D1C0A8", 12),
    NAVY_BLUE("Navy Blue", "000080", 13),
    TORQUOISE("Torquoise", "30D5C8", 14),
    MAROON("Maroon", "800000", 15),
    YELLOW("Yellow", "FFD700", 16),
    GREY("Grey", "C0C0C0", 17),
    CREAM("Cream", "FFFDD0", 18),
    OLIVE("Olive", "808000", 19),
    TEAL("Teal", "008080", 20);

    private String name;
    private String hexcode;
    private Integer ordinal;

    ColorEnum(String name, String hexcode, Integer ordinal) {
        this.name = name;
        this.hexcode = hexcode;
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

    public static Map<String, ColorEnum> getHexcodeColorEnumMap() {
        Map<String, ColorEnum> hexcodeColorMap = new HashMap<>();
        for (ColorEnum color : ColorEnum.values()) {
            hexcodeColorMap.put(color.hexcode, color);
        }
        return hexcodeColorMap;
    }


    public String getName() {
        return name;
    }

    public String getHexcode() {
        return hexcode;
    }

    public Integer getOrdinal() {
        return ordinal;
    }
}
