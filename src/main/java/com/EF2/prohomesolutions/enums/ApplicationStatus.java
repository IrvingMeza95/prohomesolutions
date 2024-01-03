package com.EF2.prohomesolutions.enums;

public enum ApplicationStatus {
    SUCCESS ("Success"),
    ERROR ("Error");

    private final String tag;

    ApplicationStatus(String tag) {
        this.tag = tag;
    }
}
