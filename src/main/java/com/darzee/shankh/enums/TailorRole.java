package com.darzee.shankh.enums;

public enum TailorRole {

    ADMIN("admin"),
    EMPLOYEE("employee");
    private String value;

    TailorRole(String value) {
        this.value = value;
    }
}
