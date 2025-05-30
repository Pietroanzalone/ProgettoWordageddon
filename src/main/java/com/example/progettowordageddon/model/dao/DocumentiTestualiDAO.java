package com.example.progettowordageddon.model.dao;

import com.example.progettowordageddon.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentiTestualiDAO {

    public static List<DocumentoTestuale> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT * FROM DocumentiTestuali");
        List<DocumentoTestuale> documenti = new ArrayList<>();
        for (var riga : risultatoQuery)
            documenti.add(generaDocumentoTestuale(riga));
        return documenti;
    }

    public static void aggiungi(DocumentoTestuale doc) throws SQLException {
        DAO.eseguiUpdate("""
            INSERT INTO DocumentiTestuali
            (nome, lingua, difficolta, testo)
            VALUES (?, ?, ?, ?)
        """, doc.getNome(), doc.getLingua().name(), doc.getDifficolta().name(), doc.getTesto());
    }

    public static void rimuovi(String nome) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM DocumentiTestuali WHERE nome = ?", nome);
    }

    public static DocumentoTestuale get(String nome) throws SQLException {
        var righe = DAO.eseguiSelect(
            "SELECT * FROM DocumentiTestuali WHERE nome = ?", nome);
        if (righe.isEmpty()) return null;
        return generaDocumentoTestuale(righe.get(0));
    }

    public static void aggiornaNome(String nomeVecchio, String nomeNuovo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET nome = ?
            WHERE nome = ?
        """, nomeNuovo, nomeVecchio);
    }

    public static void aggiornaLingua(String nome, Lingua lingua) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET lingua = ?
            WHERE nome = ?
        """, lingua.name(), nome);
    }

    public static void aggiornaDifficolta(String nome, Difficolta difficolta) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET difficolta = ?
            WHERE nome = ?
        """, difficolta.name(), nome);
    }

    public static void aggiornaTesto(String nome, String testo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET testo = ?
            WHERE nome = ?
        """, testo, nome);
    }

    public static boolean contiene(String nome) throws SQLException {
        return (get(nome) != null);
    }

    private static DocumentoTestuale generaDocumentoTestuale(Object[] tokens) {
        return new DocumentoTestuale(
            (String) tokens[0],
            Lingua.valueOf((String) tokens[1]),
            Difficolta.valueOf((String) tokens[2]),
            (String) tokens[3]
        );
    }

}