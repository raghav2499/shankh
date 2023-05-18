package com.darzee.shankh.enums;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Language {

    @JsonProperty("english")
    ENGLISH("english", 1),

    @JsonProperty("hindi")
    HINDI("hindi", 2),

    @JsonProperty("punjabi")
    PUNJABI("punjabi", 3);
    private String value;
    private Integer ordinal;

    Language(String value, Integer ordinal) {
        this.value = value;
        this.ordinal = ordinal;
    }

    public static Map<Integer, Language> getOrdinalEnumMap() {
        Map<Integer, Language> languageEnumMap = new HashMap<>();
        languageEnumMap.put(Language.ENGLISH.ordinal, Language.ENGLISH);
        languageEnumMap.put(Language.HINDI.ordinal, Language.HINDI);
        languageEnumMap.put(Language.PUNJABI.ordinal, Language.PUNJABI);
        return languageEnumMap;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }
}
