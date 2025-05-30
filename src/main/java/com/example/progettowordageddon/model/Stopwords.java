package com.example.progettowordageddon.model;

import java.util.Set;
import java.util.HashSet;

public class Stopwords {
    private static final Set<String> stopwords = new HashSet<>();

    public static void pulisci() {
        stopwords.clear();
    }

    public static void aggiungi(String stopword) {
        stopwords.add(stopword);
    }

    public static void rimuovi(String stopword) {
        stopwords.remove(stopword);
    }

    public static boolean contiene(String parola) {
        if (parola.length() <= 1) return true;
        return stopwords.contains(parola);
    }

}