package com.example.progettowordageddon.model;

import java.util.*;

public class Archivio {
    private final Map<String, DocumentoTestuale> archivio = new HashMap<>();

    public void pulisci() {
        archivio.clear();
    }

    public void aggiungi(DocumentoTestuale doc) {
        if (doc == null || doc.getNome() == null) return;
        archivio.put(doc.getNome(), doc);
    }

    public void rimuovi(String nome) {
        archivio.remove(nome);
    }

    public DocumentoTestuale get(String nome) {
        return archivio.get(nome);
    }

    public List<DocumentoTestuale> getTutti() {
        return new ArrayList<>(archivio.values());
    }

    public boolean contiene(String nome) {
        return archivio.containsKey(nome);
    }

    public int dimensione() {
        return archivio.size();
    }

    public List<DocumentoTestuale> filtraPerLingua(Lingua lingua) {
        return archivio.values().stream()
                .filter(doc -> doc.getLingua() == lingua)
                .toList();
    }

    public List<DocumentoTestuale> filtraPerDifficolta(Difficolta difficolta) {
        return archivio.values().stream()
                .filter(doc -> doc.getDifficolta() == difficolta)
                .toList();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (!filtraPerLingua(Lingua.ITALIANO).isEmpty()) {
            sb.append("* * * ITA * * *");
            for (var doc : filtraPerLingua(Lingua.ITALIANO))
                sb.append(doc.getNome());
        }
        if (!filtraPerLingua(Lingua.INGLESE).isEmpty()) {
            sb.append("* * * ENG * * *");
            for (var doc : filtraPerLingua(Lingua.INGLESE))
                sb.append(doc.getNome());
        }
        if (!filtraPerLingua(Lingua.FRANCESE).isEmpty()) {
            sb.append("* * * FRA * * *");
            for (var doc : filtraPerLingua(Lingua.FRANCESE))
                sb.append(doc.getNome());
        }
        if (!filtraPerLingua(Lingua.SPAGNOLO).isEmpty()) {
            sb.append("* * * ESP * * *");
            for (var doc : filtraPerLingua(Lingua.SPAGNOLO))
                sb.append(doc.getNome());
        }
        return sb.toString();
    }

}