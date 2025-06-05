package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * @class UtentiDAO
 * @brief Classe per la gestione degli utenti nel database.
 *
 * Fornisce metodi per il recupero, l'inserimento, la modifica e l'eliminazione di utenti
 * dalla tabella `Utenti`, nonché per il controllo dell'esistenza di un utente.
 */
public class UtentiDAO {

    /**
     * @brief Restituisce tutti gli utenti presenti nel database.
     *
     * @return Lista di oggetti Utente.
     * @throws SQLException Se si verifica un errore nella comunicazione con il database.
     */
    public static List<Utente> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Utenti");
        List<Utente> utenti = new ArrayList<>();
        for (var riga : risultatoQuery)
            utenti.add(generaUtente(riga));
        return utenti;
    }

    /**
     * @brief Aggiunge un nuovo utente al database.
     *
     * Se la password non è già hashata, viene hashata prima dell'inserimento.
     *
     * @param utente Oggetto Utente da inserire.
     * @throws SQLException Se si verifica un errore durante l'inserimento.
     */
    public static void aggiungi(Utente utente) throws SQLException {
        if (!utente.isHashedPassword())
            utente.hash();

        DAO.eseguiUpdate("""
            INSERT INTO Utenti
            (username, password, admin)
            VALUES (?, ?, ?)
        """, utente.getUsername(), utente.getPassword(), utente.isAdmin());
    }

    /**
     * @brief Rimuove un utente dal database.
     *
     * @param username Nome utente da eliminare.
     * @throws SQLException Se si verifica un errore durante la rimozione.
     */
    public static void rimuovi(String username) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM Utenti WHERE username = ?", username);
    }

    /**
     * @brief Recupera un utente dal database dato il suo username.
     *
     * @param username Nome utente da cercare.
     * @return Oggetto Utente se trovato, altrimenti null.
     */
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

    /**
     * @brief Aggiorna lo username di un utente.
     *
     * @param usernameVecchio Vecchio username.
     * @param usernameNuovo Nuovo username da assegnare.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento.
     */
    public static void aggiornaUsername(String usernameVecchio, String usernameNuovo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET username = ?
            WHERE username = ?
        """, usernameNuovo, usernameVecchio);
    }

    /**
     * @brief Aggiorna la password di un utente.
     *
     * La password viene hashata prima di essere salvata.
     *
     * @param username Username dell'utente.
     * @param password Nuova password da impostare.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento.
     */
    public static void aggiornaPassword(String username, String password) throws SQLException {
        Utente.hashPassword(password);
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET password = ?
            WHERE username = ?
        """, password, username);
    }

    /**
     * @brief Aggiorna lo stato admin di un utente.
     *
     * @param username Username dell'utente.
     * @param admin true per rendere admin, false altrimenti.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento.
     */
    public static void aggiornaAdmin(String username, boolean admin) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET admin = ?
            WHERE username = ?
        """, admin, username);
    }

    /**
     * @brief Verifica se un utente esiste nel database.
     *
     * @param username Username da verificare.
     * @return true se l'utente esiste, false altrimenti.
     */
    public static boolean contiene(String username) {
        return (get(username) != null);
    }

    /**
     * @brief Genera un oggetto Utente a partire da una riga della tabella.
     *
     * @param tokens Array di oggetti corrispondente ai campi della riga.
     * @return Oggetto Utente generato.
     */
    private static Utente generaUtente(Object[] tokens) {
        return new Utente(
                (String) tokens[0],
                (String) tokens[1],
                true,
                ((int) tokens[2] == 1)
        );
    }

}
