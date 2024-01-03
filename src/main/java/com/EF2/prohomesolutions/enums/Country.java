package com.EF2.prohomesolutions.enums;

public enum Country {
    ARGENTINA ("Argentina"),
    COLOMBIA ("Colombia"),
    MEXICO ("Mexico"),
    OTHER ("Other");

    public String tag;

    Country(String tag) {
        this.tag = tag;
    }

}
