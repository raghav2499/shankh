package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {

    @JsonProperty("male")
    MALE("male"),
    @JsonProperty("female")
    FEMALE("female");
    private String gender;

    public static Map<String, Gender> genderEnumMap = getGenderEnumMap();

    Gender(String genderString) {
        this.gender = genderString;
    }

    public String getString() {
        return this.gender;
    }

    public static Map<String, Gender> getGenderEnumMap() {
        Map<String, Gender> genderEnumMap = new HashMap<>();
        genderEnumMap.put(MALE.gender, MALE);
        genderEnumMap.put(FEMALE.gender, FEMALE);
        return genderEnumMap;
    }


}
