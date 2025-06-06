package com.example.progettowordageddon.model;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private final List<Domanda> domande;
    private DocumentoTestuale doc0;
    private DocumentoTestuale doc1;

    public Quiz(Difficolta difficolta, Lingua lingua) throws SQLException, IllegalStateException {
        domande = new ArrayList<>();

        // Tenta 100 volte di generare un quiz
        for (int i = 0; i < 100 && domande.size() < 10; i++) {
            GeneratoreDomanda generatore;
            if (difficolta == Difficolta.DIFFICILE) {
                doc0 = getDocumentoRandom(difficolta, lingua);
                doc1 = getDocumentoRandom(difficolta, lingua, doc0);
                generatore = new GeneratoreDomanda(doc0, doc1);
            }
            else {
                doc0 = getDocumentoRandom(difficolta, lingua);
                generatore = new GeneratoreDomanda(doc0, null);
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

    private DocumentoTestuale getDocumentoRandom(Difficolta difficolta, Lingua lingua) throws SQLException {
        return getDocumentoRandom(difficolta, lingua, null);
    }

    private DocumentoTestuale getDocumentoRandom(Difficolta difficolta, Lingua lingua, DocumentoTestuale diverso) throws SQLException {
        List<DocumentoTestuale> lista = DocumentiTestualiDAO.getTutti();
        lista = lista.stream()
            .filter(doc -> doc.getDifficolta() == difficolta)
            .filter(doc -> doc.getLingua() == lingua)
            .toList();

        DocumentoTestuale doc = null;
        while (doc == null || doc.equals(diverso)) {
            if (lista.isEmpty())
                throw new IllegalStateException("Impossibile generare un quiz");
            doc = lista.get(new java.util.Random().nextInt(lista.size()));
        }
        return doc;
    }

    public DocumentoTestuale getDocumento0() {
        return doc0;
    }

    public DocumentoTestuale getDocumento1() {
        return doc1;
    }

    public List<Domanda> getDomande() {
        return domande;
    }

    public boolean getCompletato() {
        for (var domanda : domande)
            if (!domanda.isRisposta())
                return false;
        return true;
    }

    public int getPunteggio() throws IllegalStateException {
        if (!getCompletato())
            throw new IllegalStateException("Quiz non ancora completato");
        return (int) domande.stream()
            .filter(Domanda::isCorretta)
            .count();
    }

    public Difficolta getDifficolta() {
        return doc0.getDifficolta();
    }

    public Lingua getLingua() {
        return doc0.getLingua();
    }

}