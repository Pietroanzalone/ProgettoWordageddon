package com.example.progettowordageddon.model.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

class DAO {
    private static final String DB_URL = "jdbc:sqlite:WordageddonDB.db";

    private static Connection connetti() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    protected static List<Object[]> eseguiSelect(String query, Object... parametri) throws SQLException {
        try (Connection conn = connetti()) {
            // Abilita Foreign Keys
            try (Statement pragmaStmt = conn.createStatement()) {
                pragmaStmt.execute("PRAGMA foreign_keys = ON;");
            }

            // Preparazione ed esecuzione query
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < parametri.length; i++)
                    stmt.setObject(i + 1, parametri[i]);

                try (ResultSet rs = stmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int colonne = metaData.getColumnCount();
                    List<Object[]> risultati = new ArrayList<>();
                    while (rs.next()) {
                        Object[] riga = new Object[colonne];
                        for (int i = 0; i < colonne; i++)
                            riga[i] = rs.getObject(i + 1);
                        risultati.add(riga);
                    }
                    return risultati;
                }
            }
        }
    }

    protected static void eseguiUpdate(String query, Object... parametri) throws SQLException {
        try (Connection conn = connetti()) {
            // Abilita Foreign Keys
            try (Statement pragmaStmt = conn.createStatement()) {
                pragmaStmt.execute("PRAGMA foreign_keys = ON;");
            }

            // Preparazione ed esecuzione query
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < parametri.length; i++)
                    stmt.setObject(i + 1, parametri[i]);

                stmt.executeUpdate();
            }
        }
    }
}