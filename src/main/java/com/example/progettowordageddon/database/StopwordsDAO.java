package com.example.progettowordageddon.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StopwordsDAO {

    public static List<String> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Stopwords");
        List<String> elenco = new ArrayList<>();
        for (var stopword : risultatoQuery)
            elenco.add((String) stopword[0]);
        return elenco;
    }

    public static void aggiungi(String stopword) throws SQLException {
        DAO.eseguiUpdate("""
            INSERT INTO Stopwords
            (stopword)
            VALUES (?)
        """, stopword);
    }

    public static void rimuovi(String stopword) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM Stopwords WHERE stopword = ?", stopword);
    }

    public static boolean contiene(String stopword) throws SQLException {
        if (stopword == null || stopword.trim().isBlank())
            return false;
        return !DAO.eseguiSelect("SELECT * FROM Stopwords WHERE stopword = ?", stopword).isEmpty();
    }

}