package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UtentiDAO {

    public static List<Utente> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Utenti");
        List<Utente> utenti = new ArrayList<>();
        for (var riga : risultatoQuery)
            utenti.add(generaUtente(riga));
        return utenti;
    }

    public static void aggiungi(Utente utente) throws SQLException {
        if (!utente.isHashedPassword())
            utente.hash();

        DAO.eseguiUpdate("""
            INSERT INTO Utenti
            (username, password, admin)
            VALUES (?, ?, ?)
        """, utente.getUsername(), utente.getPassword(), utente.isAdmin());
    }

    public static void rimuovi(String username) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM Utenti WHERE username = ?", username);
    }

    public static Utente get(String username) throws SQLException {
        var righe = DAO.eseguiSelect(
            "SELECT * FROM Utenti WHERE username = ?", username);
        if (righe.isEmpty()) return null;
        return generaUtente(righe.get(0));
    }

    public static void aggiornaUsername(String usernameVecchio, String usernameNuovo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET username = ?
            WHERE username = ?
        """, usernameNuovo, usernameVecchio);
    }

    public static void aggiornaPassword(String username, String password) throws SQLException {
        Utente.hashPassword(password);
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET password = ?
            WHERE username = ?
        """, password, username);
    }

    public static void aggiornaAdmin(String username, boolean admin) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE Utenti
            SET admin = ?
            WHERE username = ?
        """, admin, username);
    }

    public static boolean contiene(String username) {
        try {
            return (get(username) != null);
        } catch (SQLException e) {
            Logger.error("SQL Exception: " + e.getMessage());
            return true;
        }
    }

    private static Utente generaUtente(Object[] tokens) {
        return new Utente(
            (String) tokens[0],
            (String) tokens[1],
            true,
            ((int) tokens[2] == 1)
        );
    }

}