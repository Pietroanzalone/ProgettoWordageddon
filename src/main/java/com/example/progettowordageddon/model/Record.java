package com.example.progettowordageddon.model;

import java.time.LocalDateTime;

/// @class Record
/// @brief Rappresenta una singola voce della classifica (leaderboard).
/// @ingroup model
///
/// Contiene informazioni sull'utente, punteggio ottenuto, lingua,
/// difficoltà e timestamp.
public record Record(String username, int punteggio, Lingua lingua, Difficolta difficolta, LocalDateTime timestamp) implements Comparable<Record> {

    ///@brief Costruttore a partire da un oggetto {@link Quiz}.
    ///Imposta il timestamp all'ora attuale.
    ///@param username Username dell'utente.
    ///@param quiz Quiz completato da cui si estraggono dati.
    public Record(String username, Quiz quiz){
        this(username, quiz.getPunteggio(), quiz.getLingua(), quiz.getDifficolta(), LocalDateTime.now());
    }

    /// @brief Confronta due record per l'ordinamento.
    ///
    /// La logica di ordinamento è definita per:
    /// 1. Punteggio
    /// 2. Difficoltà
    /// 3. Timestamp
    /// 4. Username
    /// @param record R%ecord da confrontare.
    /// @return Un valore negativo se questo record
    ///         precede l'altro, un valore positivo
    ///         se lo segue, `0` se sono uguali.
    @Override
    public int compareTo(Record record) {
        int punt = punteggio - record.punteggio();
        int diff = difficolta.compareTo(record.difficolta);
        int data = timestamp.compareTo(record.timestamp());
        int user = username.compareTo(record.username());

        if (punt != 0) return -punt;   // punteggio decrescente
        if (diff != 0) return -diff;   // difficoltà decrescente
        if (data != 0) return -data;   // timestamp decrescente
        return user;                   // username crescente
    }

    /// @brief Genera una rappresentazione testuale del record.
    ///
    /// Questo metodo è utilizzato nella schermata "Utente" per
    /// mostrare l'ultimo quiz tentato da un utente.
    /// Mostra un quiz con il formato `"LINGUA DIFFICOLTA PUNTEGGIO / 10"`.
    ///
    /// @return Rappresentazione testuale del record.
    @Override
    public String toString() {
        return lingua.name() + " " + difficolta.name() + " " + punteggio + " / 10";
    }

}