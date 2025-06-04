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
        DocumentoTestuale documento = (doc == 1) ? doc1 : doc2;
        Map<String, Integer> frequenze = documento.getConteggioParole();

        List<String> paroleValide = frequenze.keySet().stream()
                .filter(p -> p.length() > 3 && frequenze.get(p) > 0)
                .toList();

        if (paroleValide.isEmpty()) return null;

        String parola = paroleValide.get(random.nextInt(paroleValide.size()));
        int frequenza = frequenze.get(parola);

        Set<Integer> opzioniSet = new HashSet<>();
        opzioniSet.add(frequenza);
        while (opzioniSet.size() < 4) {
            int fake = Math.max(0, frequenza + random.nextInt(7) - 3);
            if (fake != frequenza) opzioniSet.add(fake);
        }

        List<Integer> opzioni = new ArrayList<>(opzioniSet);
        Collections.shuffle(opzioni);

        int correttaIndex = opzioni.indexOf(frequenza);

        String testoDomanda = String.format(
                "Quante volte compare la parola \"%s\" nel documento \"%s\"?",
                parola, documento.getNome()
        );

        return new Domanda(
                testoDomanda,
                String.valueOf(opzioni.get(0)),
                String.valueOf(opzioni.get(1)),
                String.valueOf(opzioni.get(2)),
                String.valueOf(opzioni.get(3)),
                correttaIndex
        );
    }


    private Domanda domandaConfronto(int docIndex) {
        DocumentoTestuale doc = (docIndex == 1) ? doc1 : doc2;
        Map<String, Integer> frequenze = doc.getConteggioParole();

        List<String> paroleValide = frequenze.keySet().stream()
                .filter(p -> p.length() > 3 && frequenze.get(p) > 0)
                .toList();

        if (paroleValide.size() < 4) return null;

        String parolaCorretta = paroleValide.stream()
                .max(Comparator.comparingInt(frequenze::get))
                .orElse(null);

        if (parolaCorretta == null) return null;

        Set<String> opzioni = new HashSet<>();
        opzioni.add(parolaCorretta);

        Random rnd = new Random();
        while (opzioni.size() < 4) {
            String casuale = paroleValide.get(rnd.nextInt(paroleValide.size()));
            if (!opzioni.contains(casuale)) {
                opzioni.add(casuale);
            }
        }

        List<String> opzioniMescolate = new ArrayList<>(opzioni);
        Collections.shuffle(opzioniMescolate);

        int corretta = opzioniMescolate.indexOf(parolaCorretta);

        String testoDomanda = "Quale di queste parole compare più volte nel documento \"" + doc.getNome() + "\"?";

        return new Domanda(
                testoDomanda,
                opzioniMescolate.get(0),
                opzioniMescolate.get(1),
                opzioniMescolate.get(2),
                opzioniMescolate.get(3),
                corretta
        );
    }


    private Domanda domandaEsclusione(int docIndex) {
        DocumentoTestuale doc = (docIndex == 1) ? doc1 : doc2;
        Map<String, Integer> frequenze = doc.getConteggioParole();

        List<String> parolePresenti = frequenze.keySet().stream()
                .filter(p -> p.length() > 3)
                .toList();

        if (parolePresenti.size() < 3) return null;

        Set<String> opzioni = new HashSet<>();
        Random rnd = new Random();

        while (opzioni.size() < 3) {
            String parola = parolePresenti.get(rnd.nextInt(parolePresenti.size()));
            opzioni.add(parola);
        }

        String esclusa;
        do {
            esclusa = generaParolaCasualePerLingua(doc.getLingua());
        } while (frequenze.containsKey(esclusa) || esclusa.length() <= 3);


        opzioni.add(esclusa);

        List<String> opzioniMescolate = new ArrayList<>(opzioni);
        Collections.shuffle(opzioniMescolate);
        int corretta = opzioniMescolate.indexOf(esclusa);

        String testoDomanda = "Quale di queste parole non compare nel documento \"" + doc.getNome() + "\"?";

        return new Domanda(
                testoDomanda,
                opzioniMescolate.get(0),
                opzioniMescolate.get(1),
                opzioniMescolate.get(2),
                opzioniMescolate.get(3),
                corretta
        );
    }

    private String generaParolaCasualePerLingua(Lingua lingua) {
        List<String> parole = parolePerLingua.getOrDefault(lingua, List.of("defaultword"));
        Random rnd = new Random();
        return parole.get(rnd.nextInt(parole.size()));
    }



    private Domanda domandaDocumentoSpecifico(int docIndex) {
        if (doc1 == null || doc2 == null) return null;

        DocumentoTestuale docA = doc1;
        DocumentoTestuale docB = doc2;

        List<String> comuni = docA.getParoleComuni(docB);
        List<String> unicheDocA = docA.getConteggioParole().keySet().stream()
                .filter(p -> !comuni.contains(p) && p.length() > 3)
                .toList();
        List<String> unicheDocB = docB.getConteggioParole().keySet().stream()
                .filter(p -> !comuni.contains(p) && p.length() > 3)
                .toList();

        Random rnd = new Random();
        boolean sceltaDocA = rnd.nextBoolean();

        String parola;
        String correttaLabel;
        if (sceltaDocA && !unicheDocA.isEmpty()) {
            parola = unicheDocA.get(rnd.nextInt(unicheDocA.size()));
            correttaLabel = docA.getNome();
        } else if (!sceltaDocA && !unicheDocB.isEmpty()) {
            parola = unicheDocB.get(rnd.nextInt(unicheDocB.size()));
            correttaLabel = docB.getNome();
        } else {
            return null;
        }

        Set<String> opzioni = new HashSet<>();
        opzioni.add(correttaLabel);
        while (opzioni.size() < 4) {
            opzioni.add("Documento " + (char) ('A' + rnd.nextInt(10)));
        }

        List<String> opzioniMescolate = new ArrayList<>(opzioni);
        Collections.shuffle(opzioniMescolate);
        int corretta = opzioniMescolate.indexOf(correttaLabel);

        String testoDomanda = "In quale documento compare la parola \"" + parola + "\"?";

        return new Domanda(
                testoDomanda,
                opzioniMescolate.get(0),
                opzioniMescolate.get(1),
                opzioniMescolate.get(2),
                opzioniMescolate.get(3),
                corretta
        );
    }
    
}