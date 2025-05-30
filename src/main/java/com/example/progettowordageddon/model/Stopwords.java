package com.example.progettowordageddon.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;

public class Stopwords {
    private static final Set<String> stopwords = new HashSet<>();

    public static void pulisci() {
        stopwords.clear();
    }

    public static void aggiungi(String parola) {
        parola = validaParola(parola);
        stopwords.add(parola);
    }

    public static void rimuovi(String parola) {
        parola = validaParola(parola);
        stopwords.remove(parola);
    }

    public static boolean contiene(String parola) {
        parola = validaParola(parola);
        if (parola.length() <= 1) return true;
        return stopwords.contains(parola);
    }

    public static void caricaLista(Collection<String> parole) {
        pulisci();
        if (parole == null || parole.isEmpty()) return;
        for (String parola : parole) aggiungi(parola);
    }

    public static Set<String> getStopwords() {
        return Collections.unmodifiableSet(stopwords);
    }

    private static String validaParola(String parola) throws IllegalArgumentException {
        if (parola == null || parola.isBlank())
            throw new IllegalArgumentException("Parola null");
        return parola.toLowerCase();
    }

}