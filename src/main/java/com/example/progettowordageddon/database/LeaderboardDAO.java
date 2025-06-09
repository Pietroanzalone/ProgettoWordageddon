package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;
import com.example.progettowordageddon.model.Record;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @class LeaderboardDAO
 * @brief Classe DAO per gestire l'accesso ai dati della leaderboard nel database.
 *
 * Fornisce metodi per recuperare, aggiungere e cercare record relativi ai quiz completati.
 */
public class LeaderboardDAO {

    /**
     * @brief Recupera tutti i record presenti nella tabella Quiz.
     * @return Lista di {@link Record} rappresentanti la leaderboard.
     * @throws SQLException Se si verifica un errore nella query al database.
     */
    public static List<Record> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Quiz");
        List<Record> classifica = new ArrayList<>();
        for (var riga : risultatoQuery)
            classifica.add(generaRecord(riga));
        return classifica;
    }

    /**
     * @brief Inserisce un nuovo record nella tabella Quiz.
     * @param record Il record da aggiungere.
     * @throws SQLException Se si verifica un errore nell'inserimento nel database.
     */
    public static void aggiungi(Record record) throws SQLException {
        DAO.eseguiUpdate("""
            INSERT INTO Quiz
            (username, punteggio, lingua, difficolta, dataora)
            VALUES (?, ?, ?, ?, ?)
        """, record.getUsername(), record.getPunteggio(), record.getLingua(), record.getDifficolta(), record.getTimestamp());
    }

    /**
     * @brief Recupera un record specifico identificato da username e timestamp.
     * @param username Username dell'utente.
     * @param timestamp Data e ora di completamento del quiz.
     * @return Il {@link Record} corrispondente o null se non trovato.
     * @throws SQLException Se si verifica un errore nella query.
     */
    public static Record get(String username, LocalDateTime timestamp) throws SQLException {
        try {
            var righe = DAO.eseguiSelect(
                    "SELECT * FROM Quiz WHERE username = ? AND dataora = ?", username, timestamp);
            if (righe.isEmpty()) return null;
            return generaRecord(righe.get(0));
        } catch (SQLException e) {
            Logger.error("SQL Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * @brief Metodo helper che trasforma un array di oggetti ottenuto dal DB in un oggetto {@link Record}.
     * @param tokens Array di oggetti contenente i valori di una riga della tabella Quiz.
     * @return Nuovo oggetto {@link Record} creato dai valori passati.
     */
    private static Record generaRecord(Object[] tokens) {
        return new Record(
                (String) tokens[0],                    // username
                (int) tokens[4],                       // punteggio
                Lingua.valueOf((String) tokens[1]),   // lingua
                Difficolta.valueOf((String) tokens[2]), // difficoltà
                LocalDateTime.parse((String) tokens[3]) // timestamp
        );
    }
}
