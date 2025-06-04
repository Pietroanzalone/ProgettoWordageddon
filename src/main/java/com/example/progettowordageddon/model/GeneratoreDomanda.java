package com.example.progettowordageddon.model;

import java.util.Random;

public class GeneratoreDomanda {
    private final DocumentoTestuale doc1;
    private final DocumentoTestuale doc2;
    private final Random random;

    public GeneratoreDomanda(DocumentoTestuale doc1, DocumentoTestuale doc2) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        random = new Random();
    }

    public Domanda generaDomanda() {
        if (doc2 == null)
            return switch (random.nextInt(3)) {
                case 1 -> domandaConfronto(1);
                case 2 -> domandaEsclusione(1);
                default -> domandaFrequenzaAssoluta(1);
            };
        else
            return switch (random.nextInt(4)) {
                case 1 -> domandaConfronto(random.nextInt(1));
                case 2 -> domandaEsclusione(random.nextInt(1));
                case 3 -> domandaDocumentoSpecifico(random.nextInt(1));
                default -> domandaFrequenzaAssoluta(random.nextInt(1));
            };
    }

    private Domanda domandaFrequenzaAssoluta(int doc) {
        // Quante volte compare la parola [PAROLA] nel documento [TITOLO]?
        return null;
    }

    private Domanda domandaConfronto(int doc) {
        // Quale di queste parole compare più volte nel documento [TITOLO]?
        return null;
    }

    private Domanda domandaEsclusione(int doc) {
        // Quale di queste parole non compare nel documento [TITOLO]?
        return null;
    }

    private Domanda domandaDocumentoSpecifico(int doc) {
        // In quale documento compare la parola [PAROLA]?
        return null;
    }

}