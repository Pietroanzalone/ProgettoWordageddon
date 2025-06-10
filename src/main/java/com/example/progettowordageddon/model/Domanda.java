package com.example.progettowordageddon.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @class Domanda
 * @brief Rappresenta una domanda a scelta multipla con quattro risposte e una risposta corretta.
 * @ingroup model
 *
 * La classe permette di gestire domande per quiz, memorizzare la risposta selezionata
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
    private final int correttaIndex;

    /** @brief Indice (0-3) della risposta selezionata, o null se non risposto. */
    private Integer selezionataIndex;

    /**
     * @brief Costruttore della classe Domanda.
     *
     * @param testoDomanda Testo della domanda.
     * @param risposta1 Prima risposta.
     * @param risposta2 Seconda risposta.
     * @param risposta3 Terza risposta.
     * @param risposta4 Quarta risposta.
     * @param correttaIndex Indice della risposta corretta (0-3).
     */
    public Domanda(String testoDomanda, String risposta1, String risposta2, String risposta3, String risposta4, int correttaIndex) {
        this.testoDomanda = testoDomanda;
        this.risposta1 = risposta1;
        this.risposta2 = risposta2;
        this.risposta3 = risposta3;
        this.risposta4 = risposta4;
        this.correttaIndex = correttaIndex;
    }

    /**
     * @brief Restituisce il testo della domanda.
     * @return Una stringa che rappresenta la domanda.
     */
    public String getTestoDomanda() {
        return testoDomanda;
    }

    /**
     * @brief Restituisce la prima opzione di risposta.
     * @return La prima risposta possibile.
     */
    public String getRisposta1() {
        return risposta1;
    }

    /**
     * @brief Restituisce la seconda opzione di risposta.
     * @return La seconda risposta possibile.
     */
    public String getRisposta2() {
        return risposta2;
    }

    /**
     * @brief Restituisce la terza opzione di risposta.
     * @return La terza risposta possibile.
     */
    public String getRisposta3() {
        return risposta3;
    }

    /**
     * @brief Restituisce la quarta opzione di risposta.
     * @return La quarta risposta possibile.
     */
    public String getRisposta4() {
        return risposta4;
    }

    /**
     * @brief Restituisce il testo della risposta corretta.
     * @return La risposta corretta tra le quattro opzioni.
     */
    public String getCorretta() {
        return switch (correttaIndex) {
            case 0 -> risposta1;
            case 1 -> risposta2;
            case 2 -> risposta3;
            case 3 -> risposta4;
            default -> null;
        };
    }

    /**
     * @brief Restituisce il testo della risposta selezionata.
     * @return La risposta selezionata, oppure `null` se non è stata ancora risposta.
     * @see isRisposta
     */
    public String getSelezionataIndex() {
        return switch (selezionataIndex) {
            case 0 -> risposta1;
            case 1 -> risposta2;
            case 2 -> risposta3;
            case 3 -> risposta4;
            default -> null;
        };
    }

    /**
     * @brief Imposta la risposta selezionata dall'utente.
     * @param selezionataIndex Indice della risposta selezionata (0-3),
     *                         oppure `null` se non è stata ancora risposta.
     */
    public void setSelezionataIndex(Integer selezionataIndex) {
        this.selezionataIndex = selezionataIndex;
    }

    /**
     * @brief Verifica se l'utente ha selezionato una risposta.
     * @return `true` se una risposta è stata selezionata, `false` altrimenti.
     */
    public boolean isRisposta() {
        return selezionataIndex != null;
    }

    /**
     * @brief Verifica se la risposta selezionata è corretta.
     * @return true se la risposta selezionata corrisponde alla risposta corretta, false altrimenti.
     */
    public boolean isCorretta() {
        return selezionataIndex == correttaIndex;
    }

    /**
     * @brief Verifica se due oggetti Domanda sono uguali.
     *
     * Due domande sono considerate uguali se il testo della domanda e la risposta corretta coincidono.
     *
     * @param obj Oggetto con cui confrontare.
     * @return true se le domande sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Domanda d))
            return false;
        return Objects.equals(testoDomanda, d.getTestoDomanda())
            && Objects.equals(getCorretta(), d.getCorretta());
    }
}
