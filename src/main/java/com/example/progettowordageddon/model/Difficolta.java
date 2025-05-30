package com.example.progettowordageddon.model;

public enum Difficolta {
    FACILE    ("★")  ,
    MEDIA     ("★★") ,
    DIFFICILE ("★★★");

    private final String codice;

    Difficolta(String codice) {
        this.codice = codice;
    }

    @Override
    public String toString() {
        return codice;
    }
}
