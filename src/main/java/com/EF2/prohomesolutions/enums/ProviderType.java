package com.EF2.prohomesolutions.enums;

public enum ProviderType {
    GASMAN ("Gasman", "Gas", "/img/gasmen.jpg"),
    ELECTRICIAN ("Electrician", "Electric", "/img/electricista.jpg"),
    PLUMBER("Plumber", "Plumbing", "/img/plomero.jpg");

    public final String tag;
    public final String job;
    public final String coverUrl;

    ProviderType(String tag, String job, String coverUrl) {
        this.tag = tag;
        this.job = job;
        this.coverUrl = coverUrl;
    }
}
