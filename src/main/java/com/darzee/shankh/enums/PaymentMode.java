package com.darzee.shankh.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, PaymentMode> getPaymentTypeEnumOrdinalMap() {
        Map<Integer, PaymentMode> paymentOrdinalEnumMap = new HashMap<>();
        paymentOrdinalEnumMap.put(CASH.ordinal, CASH);
        paymentOrdinalEnumMap.put(BANK_TRANSFER.ordinal, BANK_TRANSFER);
        paymentOrdinalEnumMap.put(CARD_SWIPE.ordinal, CARD_SWIPE);
        paymentOrdinalEnumMap.put(UPI.ordinal, UPI);
        paymentOrdinalEnumMap.put(OTHER.ordinal, OTHER);
        return paymentOrdinalEnumMap;
    }
}
