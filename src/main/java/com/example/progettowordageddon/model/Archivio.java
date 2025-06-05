package com.example.progettowordageddon.model;

import java.util.*;

/**
 * @class Archivio
 * @brief Rappresenta una collezione locale di documenti testuali indicizzati per nome.
 *
 * Questa classe permette di gestire un archivio in memoria di oggetti {@link DocumentoTestuale},
 * fornendo metodi per aggiungere, rimuovere, filtrare e accedere ai documenti.
 */
public class Archivio {

    /** @brief Mappa che associa il nome del documento al documento stesso. */
    private final Map<String, DocumentoTestuale> archivio = new HashMap<>();

    /**
     * @brief Rimuove tutti i documenti dall'archivio.
     */
    public void pulisci() {
        archivio.clear();
    }

    /**
     * @brief Aggiunge un documento all'archivio.
     *
     * Se il documento o il suo nome è nullo, l'inserimento viene ignorato.
     *
     * @param doc Documento da aggiungere.
     */
    public void aggiungi(DocumentoTestuale doc) {
        if (doc == null || doc.getNome() == null) return;
        archivio.put(doc.getNome(), doc);
    }

    /**
     * @brief Rimuove un documento dall'archivio tramite il suo nome.
     *
     * @param nome Nome del documento da rimuovere.
     */
    public void rimuovi(String nome) {
        archivio.remove(nome);
    }

    /**
     * @brief Recupera un documento dal suo nome.
     *
     * @param nome Nome del documento.
     * @return Oggetto {@link DocumentoTestuale} corrispondente, o null se non trovato.
     */
    public DocumentoTestuale get(String nome) {
        return archivio.get(nome);
    }

    /**
     * @brief Restituisce tutti i documenti contenuti nell'archivio.
     *
     * @return Lista di tutti i documenti presenti.
     */
    public List<DocumentoTestuale> getTutti() {
        return new ArrayList<>(archivio.values());
    }

    /**
     * @brief Verifica se un documento è presente nell'archivio.
     *
     * @param nome Nome del documento.
     * @return true se il documento è presente, false altrimenti.
     */
    public boolean contiene(String nome) {
        return archivio.containsKey(nome);
    }

    /**
     * @brief Restituisce il numero totale di documenti presenti nell'archivio.
     *
     * @return Numero di documenti.
     */
    public int dimensione() {
        return archivio.size();
    }

    /**
     * @brief Filtra i documenti per lingua specificata.
     *
     * @param lingua Lingua desiderata.
     * @return Lista di documenti nella lingua specificata.
     */
    public List<DocumentoTestuale> filtraPerLingua(Lingua lingua) {
        return archivio.values().stream()
                .filter(doc -> doc.getLingua() == lingua)
                .toList();
    }

    /**
     * @brief Filtra i documenti per difficoltà specificata.
     *
     * @param difficolta Difficoltà desiderata.
     * @return Lista di documenti con la difficoltà specificata.
     */
    public List<DocumentoTestuale> filtraPerDifficolta(Difficolta difficolta) {
        return archivio.values().stream()
                .filter(doc -> doc.getDifficolta() == difficolta)
                .toList();
    }

    /**
     * @brief Rappresenta l'archivio come stringa organizzata per lingua.
     *
     * La rappresentazione include i nomi dei documenti raggruppati per lingua.
     *
     * @return Stringa formattata con i nomi dei documenti per lingua.
     */
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
