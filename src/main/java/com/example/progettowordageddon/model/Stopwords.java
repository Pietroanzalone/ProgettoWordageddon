package com.example.progettowordageddon.model;

import java.util.ArrayList;
import java.util.List;

public class Stopwords {
    private static final List<String> stopwords = new ArrayList<>();

    public static void pulisci() {
        stopwords.clear();
    }

    public static void aggiungi(String stopword) {
        if (!stopwords.contains(stopword))
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