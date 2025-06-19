package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.Utente;
import com.example.progettowordageddon.model.Logger;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * @class UtentiDAO
 * @brief Classe per la gestione degli utenti nel database.
 * @ingroup database
 *
 * Fornisce metodi per il recupero, l'inserimento, la modifica e l'eliminazione di utenti
 * dalla tabella {@code Utenti}, nonché per il controllo dell'esistenza di un utente.
 *
 * <h2>Struttura della tabella `Utenti` nel database:</h2>
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Campo</th>
 *       <th>Tipo</th>
 *       <th>Vincoli</th>
 *       <th>Descrizione</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>username</td>
 *       <td>TEXT</td>
 *       <td>PRIMARY KEY</td>
 *       <td>Identificatore univoco dell'utente</td>
 *     </tr>
 *     <tr>
 *       <td>password</td>
 *       <td>TEXT</td>
 *       <td>NOT NULL</td>
 *       <td>Password dell'utente (potenzialmente hashata)</td>
 *     </tr>
 *     <tr>
 *       <td>admin</td>
 *       <td>BOOLEAN</td>
 *       <td>NOT NULL</td>
 *       <td>Indica se l'utente ha privilegi amministrativi</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * @image html ClaDia_UtentiDAO.png width=80%
 */
public final class UtentiDAO {

    /// @brief Restituisce tutti gli utenti presenti nel database.
    /// @return Lista di oggetti Utente.
    /// @throws SQLException Se si verifica un errore nella comunicazione con il database.
    public static List<Utente> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Utenti");
        List<Utente> utenti = new ArrayList<>();
        for (var riga : risultatoQuery)
            utenti.add(generaUtente(riga));
        return utenti;
    }

    /// @brief Recupera un utente dal database dato il suo username.
    /// @param username Nome utente da cercare.
    /// @return Oggetto Utente se trovato, altrimenti null.
    public static Utente get(String username) {
        try {
            var righe = DAO.eseguiSelect(
                "SELECT * FROM Utenti WHERE username = ?", username);
            if (righe.isEmpty()) return null;
            return generaUtente(righe.get(0));
        } catch (SQLException e) {
            Logger.error("SQL Exception: " + e.getMessage());
            return null;
        }
    }

    /// @brief Verifica se un utente esiste nel database.
    /// @param username Username da verificare.
    /// @return true se l'utente esiste, false altrimenti.
    public static boolean contiene(String username) {
        return (get(username) != null);
    }

    /// @brief Aggiunge un nuovo utente al database.
    ///
    /// Se la password non è già hashata, viene hashata prima dell'inserimento.
    ///
    /// @param utente Oggetto Utente da inserire.
    /// @throws SQLException Se si verifica un errore durante l'inserimento.
    public static void aggiungi(Utente utente) throws SQLException {
        if (!utente.isHashedPassword())
            utente.hash();

        DAO.eseguiUpdate("""
            INSERT INTO Utenti
            (username, password, admin)
            VALUES (?, ?, ?)
        """, utente.getUsername(), utente.getPassword(), utente.isAdmin());
    }

    /// @brief Rimuove un utente dal database.
    /// @param username Nome utente da eliminare.
    /// @throws SQLException Se si verifica un errore durante la rimozione.
    public static void rimuovi(String username) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM Utenti WHERE username = ?", username);
    }

    /// @brief Aggiorna lo stato admin di un utente.
    /// @param username Username dell'utente.
    /// @param admin true per rendere admin, false altrimenti.
    /// @throws SQLException Se si verifica un errore durante l'aggiornamento.
    public static void aggiornaAdmin(String username, boolean admin) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET admin = ?
            WHERE username = ?
        """, admin, username);
    }

    /// @brief Genera un oggetto Utente a partire da una riga della tabella.
    /// @param tokens Array di oggetti corrispondente ai campi della riga.
    /// @return Oggetto Utente generato.
    private static Utente generaUtente(Object[] tokens) {
        return new Utente(
            (String) tokens[0],
            (String) tokens[1],
            true,
            ((int) tokens[2] == 1)
        );
    }

}