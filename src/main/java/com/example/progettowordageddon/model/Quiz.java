package com.example.progettowordageddon.model;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/// @class Quiz
/// @brief Rappresenta un quiz composto da 10 domande generate a partire da uno o due documenti testuali.
///
/// Il quiz viene generato in base alla difficoltà e alla lingua specificate. Per la difficoltà
/// \link Difficolta DIFFICILE \endlink vengono usati due documenti diversi, mentre per difficoltà più
/// semplici si usa un solo documento. Il quiz contiene esattamente 10 domande generate casualmente,
/// assicurandosi che non siano ripetute.
///
/// Se il sistema non riesce a generare 10 domande valide dopo 100 tentativi, solleva un'eccezione.
public class Quiz implements Serializable {

    /// @brief Lista delle domande che compongono il quiz.
    private final List<Domanda> domande;

    /// @brief Primo documento usato per la generazione delle domande.
    private DocumentoTestuale primoDocumento;

    /// @brief Secondo documento (solo per la difficoltà `DIFFICILE`).
    private DocumentoTestuale secondoDocumento;

    /// @brief Costruisce un quiz generando 10 domande in base alla difficoltà e alla lingua.
    ///
    /// Per difficoltà DIFFICILE, usa due documenti testuali distinti.
    /// Per altre difficoltà, ne usa solo uno.
    ///
    /// @param difficolta La difficoltà del quiz.
    /// @param lingua La lingua dei documenti da cui generare le domande.
    /// @throws SQLException Se si verifica un errore nell'accesso al database.
    /// @throws IllegalStateException Se non è possibile generare 10 domande valide.
    public Quiz(Difficolta difficolta, Lingua lingua) throws SQLException, IllegalStateException {
        domande = new ArrayList<>();

        // Tenta 100 volte di generare un quiz
        for (int i = 0; i < 100 && domande.size() < 10; i++) {
            GeneratoreDomanda generatore;
            if (difficolta == Difficolta.DIFFICILE) {
                primoDocumento = getDocumentoRandom(difficolta, lingua);
                secondoDocumento = getDocumentoRandom(difficolta, lingua, primoDocumento);
                generatore = new GeneratoreDomanda(primoDocumento, secondoDocumento);
            }
            else {
                primoDocumento = getDocumentoRandom(difficolta, lingua);
                generatore = new GeneratoreDomanda(primoDocumento, null);
            }

            domande.clear();
            while (domande.size() < 10) {
                Domanda domanda = generatore.generaDomanda();
                if (!domande.contains(domanda))
                    domande.add(domanda);
            }
        }

        // Se non riesce a generare un quiz, solleva IllegalStateException
        if (domande.size() < 10)
            throw new IllegalStateException("Impossibile generare un quiz");
    }

    /// @brief Restituisce un documento casuale dalla base dati, filtrato per difficoltà e lingua.
    ///        Se specificato, evita di restituire un documento già scelto.
    ///
    /// @param difficolta La difficoltà desiderata.
    /// @param lingua La lingua desiderata.
    /// @param diverso Documento da escludere (può essere `null`).
    /// @return Un documento testuale casuale valido.
    /// @throws SQLException Se si verifica un errore nel recupero dei documenti.
    /// @throws IllegalStateException Se non esistono documenti validi con i criteri specificati.
    private DocumentoTestuale getDocumentoRandom(Difficolta difficolta, Lingua lingua, DocumentoTestuale diverso) throws SQLException, IllegalStateException {
        List<DocumentoTestuale> lista = DocumentiTestualiDAO.getTutti();
        lista = lista.stream()
            .filter(doc -> doc.getDifficolta() == difficolta)
            .filter(doc -> doc.getLingua() == lingua)
            .toList();

        DocumentoTestuale doc = null;
        while (doc == null || doc.equals(diverso)) {
            if (lista.isEmpty())
                throw new IllegalStateException("Non esistono documenti validi con i criteri specificati");
            doc = lista.get(new java.util.Random().nextInt(lista.size()));
        }
        return doc;
    }

    /// @brief Versione sovraccarica di getDocumentoRandom che non esclude alcun documento.
    ///
    /// @param difficolta La difficoltà desiderata.
    /// @param lingua La lingua desiderata.
    /// @return Un documento testuale casuale.
    /// @throws SQLException Se si verifica un errore nel recupero dei documenti.
    /// @throws IllegalStateException Se non esistono documenti validi con i criteri specificati.
    private DocumentoTestuale getDocumentoRandom(Difficolta difficolta, Lingua lingua) throws SQLException, IllegalStateException {
        return getDocumentoRandom(difficolta, lingua, null);
    }

    /// @brief Restituisce il primo documento usato per generare il quiz.
    /// @return Il documento principale ({@link primoDocumento}).
    public DocumentoTestuale getPrimoDocumento() {
        return primoDocumento;
    }

    /// @brief Restituisce il secondo documento (solo se presente).
    /// @return Il secondo documento ({@link secondoDocumento}), oppure `null` se non usato.
    public DocumentoTestuale getSecondoDocumento() {
        return secondoDocumento;
    }

    /// @brief Restituisce la lista delle domande del quiz.
    /// @return Lista di oggetti Domanda.
    public List<Domanda> getDomande() {
        return domande;
    }

    /// @brief Indica se tutte le domande del quiz hanno ricevuto una risposta.
    /// @return true se tutte le domande hanno una risposta, false altrimenti.
    public boolean isCompletato() {
        for (var domanda : domande)
            if (!domanda.isRisposta())
                return false;
        return true;
    }

    /// @brief Calcola il punteggio totale del quiz (risposte corrette).
    /// @return Numero di risposte corrette.
    /// @throws IllegalStateException Se il quiz non è ancora stato completato.
    public int getPunteggio() throws IllegalStateException {
        if (!isCompletato())
            throw new IllegalStateException("Quiz non ancora completato");
        return (int) domande.stream()
            .filter(Domanda::isCorretta)
            .count();
    }

    /// @brief Restituisce la difficoltà del quiz.
    /// @return La difficoltà dei documenti usati.
    public Difficolta getDifficolta() {
        return primoDocumento.getDifficolta();
    }

   /// @brief Restituisce la lingua del quiz.
   /// @return La lingua dei documenti usati.
    public Lingua getLingua() {
        return primoDocumento.getLingua();
    }

}