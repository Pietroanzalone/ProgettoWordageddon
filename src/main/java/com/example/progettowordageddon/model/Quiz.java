package com.example.progettowordageddon.model;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private final List<Domanda> domande;

    public Quiz(Difficolta difficolta, Lingua lingua) throws SQLException {
        domande = new ArrayList<>();
        // Tenta 100 volte di generare un quiz
        for (int i = 0; domande.size() < 10 || i < 100; i++) {
            GeneratoreDomanda generatore;
            if (difficolta == Difficolta.DIFFICILE) {
                var doc0 = getDocumentoRandom(difficolta, lingua);
                var doc1 = getDocumentoRandom(difficolta, lingua, doc0);
                generatore = new GeneratoreDomanda(doc0, doc1);
            }
            else
                generatore = new GeneratoreDomanda(getDocumentoRandom(difficolta, lingua), null);

            domande.clear();
            while (domande.size() < 10) {
                Domanda domanda = generatore.generaDomanda();
                if (!domande.contains(domanda))
                    domande.add(domanda);
            }
        }
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
        while (doc == null || doc.equals(diverso))
            doc = lista.get(new java.util.Random().nextInt(lista.size()));
        return doc;
    }


}