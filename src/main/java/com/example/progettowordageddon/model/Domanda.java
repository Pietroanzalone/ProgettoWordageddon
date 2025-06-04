package com.example.progettowordageddon.model;

import java.util.Objects;

public class Domanda {
    private final String testoDomanda;
    private final String risposta1;
    private final String risposta2;
    private final String risposta3;
    private final String risposta4;
    private final int corretta;
    private Integer selezionata;

    public Domanda(String testoDomanda, String risposta1, String risposta2, String risposta3, String risposta4, int corretta) {
        this.testoDomanda = testoDomanda;
        this.risposta1 = risposta1;
        this.risposta2 = risposta2;
        this.risposta3 = risposta3;
        this.risposta4 = risposta4;
        this.corretta = corretta;
    }

    public void setSelezionata(Integer selezionata) {
        this.selezionata = selezionata;
    }

    public boolean isRisposta() {
        return selezionata != null;
    }

    public boolean isCorretta() {
        return selezionata == corretta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testoDomanda, corretta);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Domanda d)
            return hashCode() == d.hashCode();
        return false;
    }

    public void chiediDomanda() {
        System.out.println(testoDomanda);
        System.out.println("A: " + risposta1);
        System.out.println("B: " + risposta2);
        System.out.println("C: " + risposta3);
        System.out.println("D: " + risposta4);
        System.out.println("Corretta: " + corretta);
    }

}