package com.example.progettowordageddon.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;

/**
 * @class Stopwords
 * @brief La collezione di parole da ignorare per i quiz.
 *
 * Classe utility che fornisce metodi statici per aggiungere, rimuovere,
 * controllare e ottenere le stopwords del quiz.
 *
 * Tutte le parole con una singola lettera sono automaticamente trattate
 * come stopwords.
 */
public class Stopwords {
    /** @brief Collezione contenente l'insieme delle stopwords */
    private static final Set<String> stopwords;

    /** \cond DOXY_SKIP */
    static {
        stopwords = new HashSet<>();
    }
    /** \endcond */

    /**
     * @brief Pulisce completamente la lista delle stopword.
     *
     * Rimuove tutte le parole attualmente presenti nella lista.
     */
    public static void pulisci() {
        stopwords.clear();
    }

    /**
     * @brief Aggiunge una parola alla lista delle stopword.
     *
     *
     * @param parola La parola da aggiungere.
     * @throws IllegalArgumentException se la parola è nulla o vuota.
     */
    public static void aggiungi(String parola) {
        parola = validaParola(parola);
        stopwords.add(parola);
    }

    /**
     * @brief Rimuove una parola dalla lista delle stopword.
     *
     *
     * @param parola La parola da rimuovere.
     * @throws IllegalArgumentException se la parola è nulla o vuota.
     */
    public static void rimuovi(String parola) {
        parola = validaParola(parola);
        stopwords.remove(parola);
    }

    /**
     * @brief Controlla se una parola è presente nella lista delle stopword.
     *
     * Le parole con lunghezza 1 o inferiore sono considerate stopword per convenzione.
     *
     * @param parola La parola da verificare.
     * @return true se è presente o è troppo corta, false altrimenti.
     * @throws IllegalArgumentException se la parola è nulla o vuota.
     */
    public static boolean contiene(String parola) {
        parola = validaParola(parola);
        if (parola.length() <= 1) return true;
        return stopwords.contains(parola);
    }

    /**
     * @brief Carica una nuova lista di stopword.
     *
     * Svuota la lista attuale e carica tutte le parole della collezione fornita.
     *
     * @param parole Una collezione di stringhe da impostare come stopword.
     */
    public static void caricaLista(Collection<String> parole) {
        pulisci();
        if (parole == null || parole.isEmpty()) return;
        for (String parola : parole) aggiungi(parola);
    }

    /**
     * @brief Restituisce una vista non modificabile della lista di stopword.
     *
     * @return Un Set immutabile contenente le stopword attualmente caricate.
     */
    public static Set<String> getStopwords() {
        return Collections.unmodifiableSet(stopwords);
    }

    /**
     * @brief Valida una parola.
     *
     * Converte la parola in minuscolo e verifica che non sia nulla o vuota.
     *
     * @param parola La parola da validare.
     * @return La parola in minuscolo.
     * @throws IllegalArgumentException se la parola è nulla o vuota.
     */
    private static String validaParola(String parola) throws IllegalArgumentException {
        if (parola == null || parola.isBlank())
            throw new IllegalArgumentException("Parola null");
        return parola.toLowerCase();
    }

}