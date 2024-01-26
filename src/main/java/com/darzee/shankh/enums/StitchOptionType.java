package com.darzee.shankh.enums;

public enum StitchOptionType {

    RADIO("radio"),
    COUNTER("counter");
    private String name;

    StitchOptionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
