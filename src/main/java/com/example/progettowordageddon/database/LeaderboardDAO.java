package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;
import com.example.progettowordageddon.model.Record;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LeaderboardDAO {
    public static List <Record> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM Quiz");
        List<Record> classifica = new ArrayList<>();
        for (var riga : risultatoQuery)
            classifica.add(generaRecord(riga));
        return classifica;
    }

    public static void aggiungi(Record record) throws SQLException {
        DAO.eseguiUpdate("""
            INSERT INTO Quiz
            (username, punteggio, lingua,  difficolta, dataora)
            VALUES (?, ?, ?, ?, ?)
        """, record.getUsername(), record.getPunteggio(), record.getLingua(), record.getDifficolta(), record.getTimestamp());
    }

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

    private static Record generaRecord(Object[] tokens) {
        return new Record(
                (String) tokens[0],
                (int) tokens[4],
                Lingua.valueOf((String) tokens[1]),
                Difficolta.valueOf((String) tokens[2]),
                LocalDateTime.parse((String) tokens[3])
        );
    }
}