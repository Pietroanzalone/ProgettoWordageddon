package com.example.progettowordageddon.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @class Domanda
 * @brief Rappresenta una domanda a scelta multipla con quattro risposte e una risposta corretta.
 *
 * La classe permette di gestire domande quiz, memorizzare la risposta selezionata
 * e verificare se la risposta data è corretta.
 */
public class Domanda implements Serializable {

    /** @brief Testo della domanda. */
    private final String testoDomanda;

    /** @brief Prima opzione di risposta. */
    private final String risposta1;

    /** @brief Seconda opzione di risposta. */
    private final String risposta2;

    /** @brief Terza opzione di risposta. */
    private final String risposta3;

    /** @brief Quarta opzione di risposta. */
    private final String risposta4;

    /** @brief Indice (0-3) della risposta corretta. */
    private final int corretta;

    /** @brief Indice (0-3) della risposta selezionata, o null se non risposto. */
    private Integer selezionata;

    /**
     * @brief Costruttore della classe Domanda.
     *
     * @param testoDomanda Testo della domanda.
     * @param risposta1 Prima risposta.
     * @param risposta2 Seconda risposta.
     * @param risposta3 Terza risposta.
     * @param risposta4 Quarta risposta.
     * @param corretta Indice della risposta corretta (0-3).
     */
    public Domanda(String testoDomanda, String risposta1, String risposta2, String risposta3, String risposta4, int corretta) {
        this.testoDomanda = testoDomanda;
        this.risposta1 = risposta1;
        this.risposta2 = risposta2;
        this.risposta3 = risposta3;
        this.risposta4 = risposta4;
        this.corretta = corretta;
    }

    public String getTestoDomanda(){
        return testoDomanda;
    }

    public String getRisposta1(){
        return risposta1;
    }

    public String getRisposta2(){
        return risposta2;
    }

    public String getRisposta3(){
        return risposta3;
    }

    public String getRisposta4(){
        return risposta4;
    }

    /**
     * @brief Imposta la risposta selezionata dall'utente.
     * @param selezionata Indice della risposta selezionata (0-3), o null.
     */
    public void setSelezionata(Integer selezionata) {
        this.selezionata = selezionata;
    }

    /**
     * @brief Verifica se una risposta è stata selezionata.
     * @return true se l'utente ha risposto, false altrimenti.
     */
    public boolean isRisposta() {
        return selezionata != null;
    }

    /**
     * @brief Verifica se la risposta selezionata è corretta.
     * @return true se la risposta selezionata corrisponde a quella corretta, false altrimenti.
     */
    public boolean isCorretta() {
        return selezionata == corretta;
    }

    /**
     * @brief Calcola l'hash della domanda basato su testo e risposta corretta.
     * @return Hash code della domanda.
     */
    @Override
    public int hashCode() {
        return Objects.hash(testoDomanda, corretta);
    }

    /**
     * @brief Verifica l'uguaglianza con un altro oggetto Domanda.
     * @param obj Oggetto da confrontare.
     * @return true se l'hash è identico, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Domanda d)
            return hashCode() == d.hashCode();
        return false;
    }

    /**
     * @brief Stampa la domanda e le sue opzioni sulla console (debug/testing).
     */
    public void chiediDomanda() {
        System.out.println(testoDomanda);
        System.out.println("A: " + risposta1);
        System.out.println("B: " + risposta2);
        System.out.println("C: " + risposta3);
        System.out.println("D: " + risposta4);
        System.out.println("Corretta: " + corretta);
    }
}
