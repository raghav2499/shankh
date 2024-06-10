package com.darzee.shankh.enums;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Language {

    @JsonProperty("english")
    ENGLISH("english", 1,"en"),

    @JsonProperty("hindi")
    HINDI("hindi", 2,"hi"),

    @JsonProperty("punjabi")
    PUNJABI("punjabi",3,"pa"),

    @JsonProperty("gujarati")
    GUJARATI("gujarati",4,"gu"),

    @JsonProperty("marathi")
    MARATHI("marathi",5,"mr"),
    @JsonProperty("telugu")
    TELUGU("telugu",6,"te"),
    @JsonProperty("bengali")
    BENGALI("bengali",7,"bn"),
    @JsonProperty("kannada")
    KANNADA("kannada",8,"kn"),
    @JsonProperty("malyalam")
    MALYALAM("malyalam",9,"ml"),
    @JsonProperty("odia")
    ODIA("odia",10,"or"),
    @JsonProperty("assamese")
    ASSAMESE("assamese",11,"as"),
    @JsonProperty("tamil")
    TAMIL("tamil",12,"ta"),
    @JsonProperty("urdu")
    URDU("urdu",13,"ur");

    private String value;

    private String notation;
    private Integer ordinal;

    Language(String value, Integer ordinal, String notation) {
        this.value = value;
        this.ordinal = ordinal;
        this.notation = notation;
    }

    public static Map<Integer, Language> getOrdinalEnumMap() {
        Map<Integer, Language> languageOrdinalEnumMap = new HashMap<>();
        for (Language lang : Language.values()) {
            languageOrdinalEnumMap.put(lang.ordinal, lang);
        }
        return languageOrdinalEnumMap;
    }
    public static Map<String,Language> getNotationEnumMap() {
        Map<String, Language> languageNotationEnumMap = new HashMap<>();
        for (Language lang : Language.values()) {
            languageNotationEnumMap.put(lang.notation, lang);
        }
        return languageNotationEnumMap;
    }

    public Integer getOrdinal() {
        return this.ordinal;
    }
    public String getNotation() {
        return this.notation;
    }
}
