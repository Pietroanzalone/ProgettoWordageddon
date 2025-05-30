package com.example.progettowordageddon.model;

public enum Lingua {
    ITALIANO ("ITA"),
    INGLESE  ("ENG"),
    FRANCESE ("FRA"),
    SPAGNOLO ("ESP");

    private final String codice;

    Lingua(String codice) {
        this.codice = codice;
    }

    @Override
    public String toString() {
        return codice;
    }
}
