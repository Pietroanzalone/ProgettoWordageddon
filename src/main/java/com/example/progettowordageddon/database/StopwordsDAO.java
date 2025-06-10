package com.example.progettowordageddon.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @class StopwordsDAO
 * @brief Classe per la gestione delle stopwords nel database.
 * @ingroup database
 *
 * Fornisce metodi per il recupero, l'inserimento e l'eliminazione di parole
 * dalla tabella {@code Stopwords}.
 *
 * <h2>Struttura della tabella `Stopwords` nel database:</h2>
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Campo</th>
 *       <th>Tipo</th>
 *       <th>Vincolo 1</th>
 *       <th>Descrizione</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>stopword</td>
 *       <td>TEXT</td>
 *       <td>PRIMARY KEY</td>
 *       <td>Parola da ignorare nel quiz</td>
 *     </tr>
 *   </tbody>
 * </table>
 */
public class StopwordsDAO {

    /**
     * @brief Restituisce tutte le stopword presenti nel database.
     *
     * Se la tabella è vuota, inserisce un set predefinito di stopword (le lettere A-Z).
     *
     * @return Lista di stopword come stringhe.
     * @throws SQLException Se si verifica un errore nella comunicazione con il database.
     */
    public static List<String> getTutti() throws SQLException {
        defaultStopwords();

        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Stopwords");
        List<String> elenco = new ArrayList<>();
        for (var stopword : risultatoQuery)
            elenco.add((String) stopword[0]);
        return elenco;
    }

    /**
     * @brief Aggiunge una nuova stopword al database.
     *
     * La stringa viene validata (spazi rimossi, convertita in minuscolo) prima dell'inserimento.
     *
     * @param stopword La stopword da inserire.
     * @throws SQLException Se si verifica un errore durante l'inserimento nel database.
     */
    public static void aggiungi(String stopword) throws SQLException {
        stopword = validaStopword(stopword);

        DAO.eseguiUpdate("""
            INSERT INTO Stopwords
            (stopword)
            VALUES (?)
        """, stopword);
    }

    /**
     * @brief Rimuove una stopword dal database.
     *
     * La stringa viene validata prima della rimozione.
     *
     * @param stopword La stopword da rimuovere.
     * @throws SQLException Se si verifica un errore durante l'eliminazione dal database.
     */
    public static void rimuovi(String stopword) throws SQLException {
        stopword = validaStopword(stopword);

        DAO.eseguiUpdate("DELETE FROM Stopwords WHERE stopword = ?", stopword);
    }

    /**
     * @brief Verifica se una stopword è presente nel database.
     *
     * La stringa viene validata prima del controllo.
     *
     * @param stopword La stopword da verificare.
     * @return true se la stopword è presente, false altrimenti.
     */
    public static boolean contiene(String stopword) {
        stopword = validaStopword(stopword);
        if (stopword == null) return false;

        try {
            return !DAO.eseguiSelect("SELECT * FROM Stopwords WHERE stopword = ?", stopword).isEmpty();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * @brief Inserisce stopword predefinite (lettere A-Z) se non presenti.
     *
     * Questo metodo è utilizzato internamente per garantire una base minima di stopword.
     *
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    private static void defaultStopwords() throws SQLException {
        for (char c = 'A'; c <= 'Z'; c++)
            if (!contiene(Character.toString(c)))
                aggiungi(Character.toString(c));

        if (!contiene("è"))
            aggiungi("è");
    }

    /**
     * @brief Valida e normalizza una stopword.
     *
     * Rimuove gli spazi e converte la stringa in minuscolo. Restituisce `null`
     * se la stringa è nulla o vuota.
     *
     * @param stopword La stopword da validare.
     * @return La stopword normalizzata, oppure null se non valida.
     */
    private static String validaStopword(String stopword) {
        if (stopword == null || stopword.trim().isBlank()) return null;
        return stopword.trim().toLowerCase();
    }
}
