package com.example.progettowordageddon.model;

import java.util.*;

public class GeneratoreDomanda {
    private final DocumentoTestuale doc1;
    private final DocumentoTestuale doc2;
    private final Random random;
    private static final Map<Lingua, List<String>> parolePerLingua = Map.of(
        Lingua.ITALIANO, List.of(
            "lampadina", "campanile", "scrivania", "bicicletta", "calzino", "bottiglia",
            "gelato", "tavolo", "matita", "telefono", "mare", "montagna", "valigia", "cuscino",
            "ombrello", "computer", "finestra", "libro", "quaderno", "semaforo", "cappotto", "scarpa"
        ),
        Lingua.INGLESE, List.of(
            "umbrella", "notebook", "giraffe", "bottle", "window", "keyboard",
            "pencil", "mountain", "suitcase", "icecream", "desk", "lamp", "shoe", "jacket",
            "phone", "cushion", "train", "mirror", "camera", "chair", "book", "traffic"
        ),
        Lingua.FRANCESE, List.of(
            "chapeau", "voiture", "fenêtre", "bouteille", "table", "chaussette",
            "lampe", "montagne", "glace", "valise", "coussin", "chaise", "portable", "livre",
            "stylo", "chemise", "écharpe", "sac", "ordinateur", "gâteau", "boulangerie"
        ),
        Lingua.SPAGNOLO, List.of(
            "ventana", "bicicleta", "sombrero", "botella", "camisa", "reloj",
            "lampara", "cuaderno", "montaña", "helado", "almohada", "escritorio", "telefono",
            "ordenador", "librería", "mesa", "zapato", "chaqueta", "tren", "bolso", "pastel"
        )
    );

    public GeneratoreDomanda(DocumentoTestuale doc1, DocumentoTestuale doc2) {
        if (doc1 == null)
            throw new IllegalArgumentException("Il primo documento non può essere null");

        this.doc1 = doc1;
        this.doc2 = doc2;
        random = new Random();
    }

    public Domanda generaDomanda() {
        Domanda domanda = null;
        int tentativi = 0;
        while (domanda == null && tentativi < 100) {
            if (doc2 == null)
                domanda = switch (random.nextInt(3)) {
                    case 1 -> domandaConfronto(getRandomIndex());
                    case 2 -> domandaEsclusione(getRandomIndex());
                    default -> domandaFrequenzaAssoluta(getRandomIndex());
                };
            else
                domanda = switch (random.nextInt(4)) {
                    case 1 -> domandaConfronto(getRandomIndex());
                    case 2 -> domandaEsclusione(getRandomIndex());
                    case 3 -> domandaDocumentoSpecifico(getRandomIndex());
                    default -> domandaFrequenzaAssoluta(getRandomIndex());
                };
            tentativi++;
        }
        if (domanda == null)
            throw new IllegalStateException("Impossibile generare una domanda valida");
        return domanda;
    }

    private Domanda domandaFrequenzaAssoluta(int docIndex) {
        var documento = getDocumento(docIndex);

        // Ottengo tutte le parole del documento con la loro frequenza
        Map<String, Integer> frequenze = documento.getConteggioParole();
        List<String> parole = new ArrayList<>(frequenze.keySet());

        // Scelgo una parola a caso nel documento
        String parola = parole.get(random.nextInt(parole.size()));
        int frequenza = frequenze.get(parola);

        // Scelgo quattro opzioni a caso, di cui una corretta
        var opzioni = new ArrayList<Integer>();
        opzioni.add(frequenza);
        while (opzioni.size() < 4) {
            int fake = Math.max(0, frequenza + random.nextInt(7) - 3);
            if (fake != frequenza) opzioni.add(fake);
        }
        Collections.shuffle(opzioni);

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "Quante volte compare la parola \"%s\" nel documento \"%s\"?",
            parola, documento.getNome()
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            String.valueOf(opzioni.get(0)),
            String.valueOf(opzioni.get(1)),
            String.valueOf(opzioni.get(2)),
            String.valueOf(opzioni.get(3)),
            opzioni.indexOf(frequenza)
        );
    }

    private Domanda domandaConfronto(int docIndex) {
        var documento = getDocumento(docIndex);

        // Ottengo tutte le parole del documento con la loro frequenza
        Map<String, Integer> frequenze = documento.getConteggioParole();
        List<String> parole = new ArrayList<>(frequenze.keySet());

        // Trovo la parola che compare più volte nel documento
        String parolaFrequente = documento.getParolePiuFrequenti()
                        .get(random.nextInt(documento.getParolePiuFrequenti().size()));
        int frequenzaMax = frequenze.get(parolaFrequente);

        // Scelgo a caso altre 3 parole che hanno frequenza minore
        var opzioni = new ArrayList<String>();
        opzioni.add(parolaFrequente);

        while (opzioni.size() < 4) {
            String parola = parole.get(random.nextInt(parole.size()));
            if (!parola.equals(parolaFrequente) && frequenze.get(parola) < frequenzaMax)
                opzioni.add(parola);
        }
        Collections.shuffle(opzioni);

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "Quale di queste parole compare più volte nel documento \"%s\"?",
            documento.getNome()
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            opzioni.get(0),
            opzioni.get(1),
            opzioni.get(2),
            opzioni.get(3),
            opzioni.indexOf(parolaFrequente)
        );
    }

    private Domanda domandaEsclusione(int docIndex) {
        var documento = getDocumento(docIndex);

        // Ottengo tutte le parole nel documento
        List<String> parole = new ArrayList<>(documento.getConteggioParole().keySet());

        // Ottengo una parola a caso non presente nel documento
        String parolaNonPresente = null;
        Collections.shuffle(parolePerLingua.get(documento.getLingua()));
        for (var parola : parolePerLingua.get(documento.getLingua()))
            if (!documento.contiene(parola)) {
                parolaNonPresente = parola;
                break;
            }

        // Se tutte le parole disponibili sono nel documento,
        // annulla la creazione e restituisci null
        if (parolaNonPresente == null)
            return null;

        // Scelgo le quattro opzioni
        var opzioni = new ArrayList<String>();
        opzioni.add(parolaNonPresente);
        while (opzioni.size() < 4) {
            String parola = parole.get(random.nextInt(parole.size()));
            if (!opzioni.contains(parola))
                opzioni.add(parola);
        }
        Collections.shuffle(opzioni);

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "Quale di queste parole non è presente nel documento \"%s\"?",
            documento.getNome()
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            opzioni.get(0),
            opzioni.get(1),
            opzioni.get(2),
            opzioni.get(3),
            opzioni.indexOf(parolaNonPresente)
        );
    }

    private Domanda domandaDocumentoSpecifico(int docIndex) {
        var documento = getDocumento(docIndex);
        var altro = getDocumento(1 - docIndex);

        // Ottengo la lista delle parole comuni
        List<String> paroleComuni = documento.getParoleComuni(altro);

        // Ottengo la lista delle parole del primo documento
        // e rimuovo quelle comuni
        List<String> paroleEsclusive = documento.getConteggioParole().keySet().stream()
                .filter(parola -> (!paroleComuni.contains(parola)))
                .toList();

        // Se non ci sono parole esclusive,
        // annulla la creazione e restituisci null
        if (paroleEsclusive.isEmpty()) return null;

        // Scelgo una parola a caso tra quelle esclusive
        String parola = paroleEsclusive.get(random.nextInt(paroleEsclusive.size()));

        // Compongo le quattro opzioni
        var opzioni = new ArrayList<String>();
        opzioni.add(documento.getNome());
        opzioni.add(altro.getNome());
        opzioni.add("Entrambi");
        opzioni.add("Nessuno dei due");
        Collections.shuffle(opzioni);

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "In quale documento compare la parola \"%s\"?",
            parola
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            opzioni.get(0),
            opzioni.get(1),
            opzioni.get(2),
            opzioni.get(3),
            opzioni.indexOf(documento.getNome())
        );
    }

    private int getRandomIndex() {
        return random.nextInt(doc2 == null ? 1 : 2);
    }

    private DocumentoTestuale getDocumento(int index) {
        return (index == 0) ? doc1 : doc2;
    }
    
}