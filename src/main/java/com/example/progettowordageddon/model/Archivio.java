package com.example.progettowordageddon.model;

import java.util.ArrayList;

public class Archivio {
    private final ArrayList<DocumentoTestuale> archivio = new ArrayList<>();

    public void pulisci() {
        archivio.clear();
    }

    public void aggiungi(DocumentoTestuale doc) {
        archivio.add(doc);
    }

    public void rimuovi(String nome) {
        archivio.removeIf(doc -> doc.getNome().equals(nome));
    }

    public DocumentoTestuale get(String nome) {
        for (DocumentoTestuale doc : archivio)
            if (doc.getNome().equals(nome))
                return doc;
        return null;
    }
}