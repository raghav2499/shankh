package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {

    @JsonProperty("male")
    MALE("male"),
    @JsonProperty("female")
    FEMALE("female");
    private String gender;

    Gender(String genderString) {
        this.gender = genderString;
    }

    public String getString() {
        return this.gender;
    }
}
