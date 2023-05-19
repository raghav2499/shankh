package com.darzee.shankh.enums;

public enum OrderStage {
    ACTIVE("active", 1),
    CLOSED("closed", 2),
    OTHER("other", 3);

    private String stage;
    private Integer ordinal;

    OrderStage(String stage, Integer ordinal) {
        this.stage = stage;
        this.ordinal = ordinal;
    }
}
