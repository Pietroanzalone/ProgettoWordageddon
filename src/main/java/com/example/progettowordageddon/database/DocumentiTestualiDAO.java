package com.example.progettowordageddon.database;

import com.example.progettowordageddon.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @class DocumentiTestualiDAO
 * @brief Classe per la gestione dei documenti testuali nel database.
 * @ingroup database
 *
 * Fornisce metodi per il recupero, l'inserimento, la modifica e l'eliminazione di documenti
 * dalla tabella {@code DocumentiTestuali}, nonché per la ricerca tramite indice sulla lingua.
 *
 * <h2>Struttura della tabella `DocumentiTestuali` nel database:</h2>
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Campo</th>
 *       <th>Tipo</th>
 *       <th colspan="2">Vincoli</th>
 *       <th>Descrizione</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td><code>nome</code></td>
 *       <td>TEXT</td>
 *       <td colspan="2">PRIMARY KEY</td>
 *       <td>Nome univoco del documento</td>
 *     </tr>
 *     <tr>
 *       <td><code>lingua</code></td>
 *       <td>TEXT</td>
 *       <td style="width = 50%">NOT NULL</td>
 *       <td style="width = 50%">CHECK IN ('ITALIANO', 'INGLESE', 'FRANCESE', 'SPAGNOLO')</td>
 *       <td>Lingua del documento</td>
 *     </tr>
 *     <tr>
 *       <td><code>difficolta</code></td>
 *       <td>TEXT</td>
 *       <td>NOT NULL</td>
 *       <td>CHECK IN ('FACILE', 'MEDIA', 'DIFFICILE')</td>
 *       <td>Livello di difficoltà del documento</td>
 *     </tr>
 *     <tr>
 *       <td><code>testo</code></td>
 *       <td>CLOB</td>
 *       <td colspan="2">NOT NULL</td>
 *       <td>Contenuto testuale del documento</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * <p><b>Indici creati:</b></p>
 * <ul>
 *   <li><code>idx_doc_lingua</code> su campo <code>lingua</code> per velocizzare le query per lingua</li>
 * </ul>
 */
public class DocumentiTestualiDAO {

    /// @brief Restituisce tutti i documenti testuali presenti nel database.
    ///
    /// @return Lista di oggetti DocumentoTestuale.
    /// @throws SQLException se si verifica un errore durante l'accesso al database.
    public static List<DocumentoTestuale> getTutti() throws SQLException {
        List<Object[]> risultatoQuery = DAO.eseguiSelect("SELECT nome, lingua, difficolta FROM DocumentiTestuali");
        List<DocumentoTestuale> documenti = new ArrayList<>();
        for (var riga : risultatoQuery)
            documenti.add(generaDocumentoTestualeSenzaTesto(riga));
        return documenti;
    }

    /// @brief Aggiunge un nuovo documento testuale al database.
    ///
    /// @param doc Oggetto DocumentoTestuale da inserire.
    /// @throws SQLException se si verifica un errore durante l'inserimento.
    public static void aggiungi(DocumentoTestuale doc) throws SQLException {
        DAO.eseguiUpdate("""
            INSERT INTO DocumentiTestuali
            (nome, lingua, difficolta, testo)
            VALUES (?, ?, ?, ?)
        """, doc.getNome(), doc.getLingua().name(), doc.getDifficolta().name(), doc.getTesto());
    }

    /// @brief Rimuove un documento testuale dal database.
    ///
    /// @param nome Nome del documento da eliminare.
    /// @throws SQLException se si verifica un errore durante la rimozione.
    public static void rimuovi(String nome) throws SQLException {
        DAO.eseguiUpdate("DELETE FROM DocumentiTestuali WHERE nome = ?", nome);
    }

    /// @brief Recupera un documento testuale dato il suo nome.
    ///
    /// @param nome Nome del documento da cercare.
    /// @return Oggetto DocumentoTestuale se trovato, altrimenti null.
    /// @throws SQLException se si verifica un errore durante la query.
    public static DocumentoTestuale get(String nome) throws SQLException {
        var righe = DAO.eseguiSelect(
                "SELECT * FROM DocumentiTestuali WHERE nome = ?", nome);
        if (righe.isEmpty()) return null;
        return generaDocumentoTestuale(righe.get(0));
    }

    /// @brief Aggiorna il nome di un documento testuale.
    ///
    /// @param nomeVecchio Vecchio nome del documento.
    /// @param nomeNuovo Nuovo nome da assegnare.
    /// @throws SQLException se si verifica un errore durante l'aggiornamento.
    public static void aggiornaNome(String nomeVecchio, String nomeNuovo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET nome = ?
            WHERE nome = ?
        """, nomeNuovo, nomeVecchio);
    }

    /// @brief Aggiorna la lingua di un documento testuale.
    ///
    /// @param nome Nome del documento.
    /// @param lingua Nuova lingua da assegnare.
    /// @throws SQLException se si verifica un errore durante l'aggiornamento.
    public static void aggiornaLingua(String nome, Lingua lingua) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET lingua = ?
            WHERE nome = ?
        """, lingua.name(), nome);
    }

    /// @brief Aggiorna la difficoltà di un documento testuale.
    ///
    /// @param nome Nome del documento.
    /// @param difficolta Nuova difficoltà da assegnare.
    /// @throws SQLException se si verifica un errore durante l'aggiornamento.
    public static void aggiornaDifficolta(String nome, Difficolta difficolta) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET difficolta = ?
            WHERE nome = ?
        """, difficolta.name(), nome);
    }

    /// @brief Aggiorna il testo contenuto in un documento testuale.
    ///
    /// @param nome Nome del documento.
    /// @param testo Nuovo testo da inserire.
    /// @throws SQLException se si verifica un errore durante l'aggiornamento.
    public static void aggiornaTesto(String nome, String testo) throws SQLException {
        DAO.eseguiUpdate("""
            UPDATE DocumentiTestuali
            SET testo = ?
            WHERE nome = ?
        """, testo, nome);
    }

    /// @brief Verifica se un documento testuale esiste nel database.
    ///
    /// @param nome Nome del documento da cercare.
    /// @return true se esiste, false altrimenti.
    /// @throws SQLException se si verifica un errore durante la query.
    public static boolean contiene(String nome) throws SQLException {
        return (get(nome) != null);
    }

    /// @brief Crea un oggetto DocumentoTestuale a partire da una riga della tabella.
    ///
    /// @param tokens Array di oggetti corrispondenti ai campi della riga.
    /// @return Oggetto DocumentoTestuale generato.
    private static DocumentoTestuale generaDocumentoTestuale(Object[] tokens) {
        return new DocumentoTestuale(
                (String) tokens[0],
                Lingua.valueOf((String) tokens[1]),
                Difficolta.valueOf((String) tokens[2]),
                (String) tokens[3]
        );
    }

    /// @brief Crea un oggetto DocumentoTestuale a partire da una riga della tabella senza il testo.
    ///
    /// @param tokens Array di oggetti corrispondenti ai campi della riga.
    /// @return Oggetto DocumentoTestuale generato con testo vuoto.
    private static DocumentoTestuale generaDocumentoTestualeSenzaTesto(Object[] tokens) {
        return new DocumentoTestuale(
                (String) tokens[0],
                Lingua.valueOf((String) tokens[1]),
                Difficolta.valueOf((String) tokens[2]),
                ""
        );
    }

}