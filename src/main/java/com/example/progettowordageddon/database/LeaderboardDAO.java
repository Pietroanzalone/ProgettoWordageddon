package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;
import com.example.progettowordageddon.model.Record;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @class LeaderboardDAO
 * @brief Classe per la gestione dei quiz nel database.
 * @ingroup database
 *
 * Fornisce metodi per il recupero, l'inserimento e la ricerca di record dalla tabella {@code Quiz}
 *
 * <h2>Struttura della tabella `Quiz` nel database:</h2>
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Campo</th>
 *       <th>Tipo</th>
 *       <th colspan="2">Vincoli</th>
 *       <th>Descrizione</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *       <tr>
 *           <td><code>username</code></td>
 *           <td>TEXT</td>
 *           <td style="width = 50%">PRIMARY KEY</td>
 *           <td style="width = 50%">FOREIGN KEY</td>
 *           <td>Username del giocatore che ha effettuato il Quiz</td>
 *       </tr>
 *       <tr>
 *           <td><code>lingua</code></td>
 *           <td>TEXT</td>
 *            <td style="width = 50%">NOT NULL</td>
 *            <td style="width = 50%">CHECK IN ('ITALIANO', 'INGLESE', 'FRANCESE', 'SPAGNOLO')</td>
 *            <td>Lingua a cui fa riferimento il testo del quiz</td>
 *       </tr>
 *       <tr>
 *         <td><code>difficolta</code></td>
 *         <td>TEXT</td>
 *         <td>NOT NULL</td>
 *         <td>CHECK IN ('FACILE', 'MEDIA', 'DIFFICILE')</td>
 *         <td>Livello di difficoltà del documento</td>
 *       </tr>
 *       <tr>
 *           <td><code>dataora</code></td>
 *           <td>DATETIME</td>
 *           <td colspan="2">PRIMARY KEY</td>
 *           <td>Data e ora in cui il quiz è stato effettuato</td>
 *       </tr>
 *       <tr>
 *           <td><code>punteggio</code></td>
 *           <td>INTEGER</td>
 *           <td colspan="2">NOT NULL</td>
 *           <td>Punteggio ottenuto nel quiz</td>
 *       </tr>
 *
 *   </tbody>
 * </table>
 *
 *
 */
public class LeaderboardDAO {

    /**
     * @brief Recupera tutti i record presenti nella tabella Quiz.
     * @return Lista di record rappresentanti la leaderboard.
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
     * @return Il record corrispondente o null se non trovato.
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
     * @brief Metodo helper che trasforma un array di oggetti ottenuto dal DB in un record.
     * @param tokens Array di oggetti contenente i valori di una riga della tabella Quiz.
     * @return Nuovo record creato dai valori passati.
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
