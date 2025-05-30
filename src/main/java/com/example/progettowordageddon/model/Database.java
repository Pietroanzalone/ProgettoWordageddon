package com.example.progettowordageddon.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {

    private Connection connection;
    private Logger logger =Logger.getLogger(Database.class.getName());


    public  void getConnection() {
        try{
            if(connection==null || connection.isClosed()){
                connection = DriverManager.getConnection("jdbc:sqlite:WordageddonDB.db");
                logger.info("Connesso al database");
                createTable();
            }
        }catch(SQLException e){
            logger.info(e.toString());
        }
    }

    private void createTable(){
        getConnection();

        String utenteTable="CREATE TABLE IF NOT EXISTS Utenti (\n" +
                    "\tusername TEXT PRIMARY KEY,\n" +
                    "  \tpassword TEXT NOT NULL,\n" +
                    "  \tadmin BOOLEAN NOT NULL\n" +
                    ");\n";

        String documentiTable = "CREATE TABLE IF NOT EXISTS DocumentiTestuali (\n" +
                "    nome TEXT PRIMARY KEY,\n" +
                "    lingua TEXT NOT NULL,\n" +
                "    difficolta TEXT NOT NULL,\n" +
                "    testo CLOB NOT NULL,\n" +
                "  \tCHECK (lingua IN ('ITALIANO', 'INGLESE', 'FRANCESE', 'SPAGNOLO')),\n" +
                "\tCHECK (difficolta IN ('FACILE', 'MEDIA', 'DIFFICILE'))\n" +
                ");\n";

        String quizTable="CREATE TABLE IF NOT EXISTS Quiz(\n" +
                "  \tusername TEXT NOT NULL,\n" +
                "  \tlingua TEXT NOT NULL,\n" +
                "  \tdifficolta TEXT NOT NULL,\n" +
                "  \tdataora DATETIME NOT NULL,\n" +
                "  \tpunteggio INTEGER NOT NULL,\n" +
                "  \tPRIMARY KEY (username, dataora),\n" +
                "  \tFOREIGN KEY (username) REFERENCES Utenti(username) ON DELETE CASCADE,\n" +
                "  \tCHECK (lingua IN ('ITALIANO', 'INGLESE', 'FRANCESE', 'SPAGNOLO')),\n" +
                "\tCHECK (difficolta IN ('FACILE', 'MEDIA', 'DIFFICILE'))\n" +
                ");\n";

        String[] index={
                "CREATE INDEX idx_doc_lingua ON DocumentiTestuali(lingua);\n" +
                        "CREATE INDEX idx_doc_difficolta ON DocumentiTestuali(difficolta);\n" +
                        "CREATE INDEX idx_quiz_lingua ON Quiz(lingua);\n" +
                        "CREATE INDEX idx_quiz_difficolta ON Quiz(difficolta);\n"
        };


        try {
            try (PreparedStatement pst1 = connection.prepareStatement(utenteTable)) {
                pst1.executeUpdate();
                logger.info("Tabella Utenti creata");
            }

            try (PreparedStatement pst2 = connection.prepareStatement(documentiTable)) {
                pst2.executeUpdate();
                logger.info("Tabella DocumentiTestuali creata");
            }

            try (PreparedStatement pst3 = connection.prepareStatement(quizTable)) {
                pst3.executeUpdate();
                logger.info("Tabella Quiz creata");
            }

            for (String sql : index) {
                try (PreparedStatement pst = connection.prepareStatement(sql)) {
                    pst.executeUpdate();
                }
            }

            logger.info("Indici creati");

        } catch (SQLException e) {
            logger.severe("Errore durante la creazione delle tabelle o indici: " + e.getMessage());
        }
    }
}
