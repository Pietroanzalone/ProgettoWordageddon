package com.example.progettowordageddon.model;

import java.time.LocalDateTime;

/**
 * @class Record
 * @brief Rappresenta una singola voce della classifica (leaderboard).
 * @ingroup model
 *
 * Contiene informazioni sull'utente, punteggio ottenuto, lingua, difficoltà e timestamp.
 * Implementa {@link Comparable} per consentire l'ordinamento nella leaderboard.
 */
public class Record implements Comparable<Record> {

    /** @brief Username dell'utente che ha completato il quiz. */
    private String username;

    /** @brief Punteggio ottenuto dall'utente. */
    private int punteggio;

    /** @brief Lingua del quiz completato. */
    private Lingua lingua;

    /** @brief Difficoltà del quiz completato. */
    private Difficolta difficolta;

    /** @brief Data e ora in cui il quiz è stato completato. */
    private LocalDateTime timestamp;

    /**
     * @brief Costruttore completo.
     * @param username Username dell'utente.
     * @param punteggio Punteggio ottenuto.
     * @param lingua Lingua del quiz.
     * @param difficolta Difficoltà del quiz.
     * @param timestamp Data e ora di completamento.
     */
    public Record(String username, int punteggio, Lingua lingua, Difficolta difficolta, LocalDateTime timestamp) {
        this.username = username;
        this.punteggio = punteggio;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.timestamp = timestamp;
    }

    /**
     * @brief Costruttore a partire da un oggetto {@link Quiz}.
     * Imposta il timestamp all'ora attuale.
     * @param username Username dell'utente.
     * @param quiz Quiz completato da cui si estraggono dati.
     */
    public Record(String username, Quiz quiz){
        this.username = username;
        this.punteggio = quiz.getPunteggio();
        this.lingua = quiz.getLingua();
        this.difficolta = quiz.getDifficolta();
        this.timestamp = LocalDateTime.now();
    }

    /** @return Username dell'utente. */
    public String getUsername() {
        return username;
    }

    /** @param username Imposta l'username dell'utente. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return Punteggio ottenuto. */
    public int getPunteggio() {
        return punteggio;
    }

    /** @param punteggio Imposta il punteggio ottenuto. */
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /** @return Lingua del quiz. */
    public Lingua getLingua() {
        return lingua;
    }

    /** @param lingua Imposta la lingua del quiz. */
    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    /** @return Difficoltà del quiz. */
    public Difficolta getDifficolta() {
        return difficolta;
    }

    /** @param difficolta Imposta la difficoltà del quiz. */
    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    /** @return Timestamp di completamento del quiz. */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** @param timestamp Imposta il timestamp di completamento del quiz. */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @brief Confronta due record per l'ordinamento.
     * Ordinamento prioritario per:
     * 1) Punteggio (decrescente)
     * 2) Difficoltà (decrescente)
     * 3) Timestamp (decrescente)
     * 4) Username (crescente)
     * @param o Record da confrontare
     * @return valore negativo se questo record deve precedere o, positivo se dopo, 0 se uguali
     */
    @Override
    public int compareTo(Record o) {
        int punt = punteggio - o.getPunteggio();
        int diff = difficolta.compareTo(o.difficolta);
        int data = timestamp.compareTo(o.getTimestamp());
        int user = username.compareTo(o.getUsername());

        if (punt != 0) return -punt;   // punteggio decrescente
        if (diff != 0) return -diff;   // difficoltà decrescente
        if (data != 0) return -data;   // timestamp decrescente
        return user;                   // username crescente
    }
}
