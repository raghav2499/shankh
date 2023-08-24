package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorEnum {

    RED("red", 1),
    BLUE("blue", 2),
    GREEN("green", 3);

    private String name;
    private Integer ordinal;

    ColorEnum(String name, Integer ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

        public static Map<Integer, ColorEnum> outfitOrdinalEnumMap = getColorOrdinalEnumMap();

    public static Map<Integer, ColorEnum> getColorOrdinalEnumMap() {
        Map<Integer, ColorEnum> colorEnumOrdinalMap = new HashMap<>();
        colorEnumOrdinalMap.put(RED.ordinal, RED);
        colorEnumOrdinalMap.put(BLUE.ordinal, BLUE);
        colorEnumOrdinalMap.put(GREEN.ordinal, GREEN);
        return colorEnumOrdinalMap;
    }
}
