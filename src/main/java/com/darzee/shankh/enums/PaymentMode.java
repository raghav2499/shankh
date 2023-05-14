package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentMode {
    CASH("cash", 1),
    BANK_TRANSFER("Bank Transfer", 2),
    CARD_SWIPE("Card Swipe", 3),
    UPI("UPI", 4),
    OTHER("Other", 5);


    private String mode;
    private Integer ordinal;

    PaymentMode(String mode, Integer ordinal) {
        this.mode = mode;
        this.ordinal = ordinal;
    }
}
