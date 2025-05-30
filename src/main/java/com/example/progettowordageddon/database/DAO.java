package com.example.progettowordageddon.database;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @class DAO
 * @brief Classe per l'accesso al database SQLite.
 *
 * Questa classe fornisce metodi protetti statici per eseguire query di selezione
 * e aggiornamento sul database tramite JDBC.
 */
class DAO {
    /** URL di connessione al database SQLite */
    private static final String DB_URL = "jdbc:sqlite:WordageddonDB.db";

    /**
     * @brief Stabilisce una connessione al database.
     *
     * @return Oggetto Connection aperto.
     * @throws SQLException Se si verifica un errore durante la connessione.
     */
    private static Connection connetti() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * @brief Esegue una query SELECT parametrizzata e restituisce i risultati.
     *
     * Abilita le foreign key, prepara la query, imposta i parametri e
     * restituisce la lista di risultati come array di oggetti.
     *
     * @param query La query SQL SELECT da eseguire, con eventuali placeholder '?'.
     * @param parametri Parametri da sostituire ai placeholder della query.
     * @return Lista di righe risultanti, ogni riga rappresentata come array di Object.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query.
     */
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

    /**
     * @brief Esegue una query di aggiornamento (INSERT, UPDATE, DELETE) parametrizzata.
     *
     * Abilita le foreign key, prepara la query, imposta i parametri e
     * esegue l'update sul database.
     *
     * @param query La query SQL di aggiornamento da eseguire, con eventuali placeholder '?'.
     * @param parametri Parametri da sostituire ai placeholder della query.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query.
     */
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