package com.EF2.prohomesolutions.enums;

public enum FeeTypes {
    PER_HOUER ("Per hour"),
    PER_COMISSION ("Per comision");

    public String tag;

    FeeTypes(String tag) {
        this.tag = tag;
    }
}
