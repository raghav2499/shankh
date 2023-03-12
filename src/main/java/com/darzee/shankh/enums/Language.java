package com.darzee.shankh.enums;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Language {

    @JsonProperty("english")
    ENGLISH("english"),

    @JsonProperty("hindi")
    HINDI("hindi"),

    @JsonProperty("punjabi")
    PUNJABI("punjabi");
    private String value;

    Language(String value) {
        this.value = value;
    }
}
