package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentMode {
    CASH("cash", 1);

    private String mode;
    private Integer ordinal;

    PaymentMode(String mode, Integer ordinal) {
        this.mode = mode;
        this.ordinal = ordinal;
    }
}
