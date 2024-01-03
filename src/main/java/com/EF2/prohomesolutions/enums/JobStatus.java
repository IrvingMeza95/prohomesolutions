package com.EF2.prohomesolutions.enums;

public enum JobStatus {

    PENDING ("Pending"),
    CANCELED ("Canceled"),
    ACCEPTED ("Accepted"),
    IN_PROCESS ("In process"),
    COMPLETED ("Completed");

    public final String tag;

    JobStatus(String tag) {
        this.tag = tag;
    }

}
