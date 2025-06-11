package com.example.progettowordageddon.model;

import java.sql.SQLException;
import java.util.*;

/**
 * @class GeneratoreDomanda
 * @brief Il generatore di domande per il quiz.
 * @ingroup model
 *
 * Questa classe implementa un generatore randomico di domande
 * usando uno o due documenti testuali come base.
 */
public class GeneratoreDomanda {
    /** @brief Primo documento usato come base per le domande. */
    private final DocumentoTestuale primoDocumento;

    /** @brief Secondo documento usato come base per le domande.
     *
     * Nel caso di quiz di difficoltà `FACILE` o `MEDIA`, questo
     * documento è `null`.
     */
    private final DocumentoTestuale secondoDocumento;

    /** @brief Generatore di numeri random. */
    private final Random random;

    /** @brief Lista di parole utilizzate per alcuni tipi di domande. */
    private static final Map<Lingua, List<String>> parolePerLingua;

    /** \cond DOXY_SKIP */
    static {
        parolePerLingua = Map.of(
                Lingua.ITALIANO, new ArrayList<>(List.of(
                        "lampadina", "campanile", "scrivania", "bicicletta", "calzino", "bottiglia",
                        "gelato", "tavolo", "matita", "telefono", "mare", "montagna", "valigia", "cuscino",
                        "ombrello", "computer", "finestra", "libro", "quaderno", "semaforo", "cappotto", "scarpa"
                )),
                Lingua.INGLESE, new ArrayList<>(List.of(
                        "umbrella", "notebook", "giraffe", "bottle", "window", "keyboard",
                        "pencil", "mountain", "suitcase", "icecream", "desk", "lamp", "shoe", "jacket",
                        "phone", "cushion", "train", "mirror", "camera", "chair", "book", "traffic"
                )),
                Lingua.FRANCESE, new ArrayList<>(List.of(
                        "chapeau", "voiture", "fenêtre", "bouteille", "table", "chaussette",
                        "lampe", "montagne", "glace", "valise", "coussin", "chaise", "portable", "livre",
                        "stylo", "chemise", "écharpe", "sac", "ordinateur", "gâteau", "boulangerie"
                )),
                Lingua.SPAGNOLO,new ArrayList<>(List.of(
                        "ventana", "bicicleta", "sombrero", "botella", "camisa", "reloj",
                        "lampara", "cuaderno", "montaña", "helado", "almohada", "escritorio", "telefono",
                        "ordenador", "librería", "mesa", "zapato", "chaqueta", "tren", "bolso", "pastel"
                ))
        );

    }
    /** \endcond */

    /**
     * @brief Costruttore completo per la classe GeneratoreDomanda.
     *
     * @param primoDocumento Primo documento da usare come base.
     * @param secondoDocumento Secondo documento da usare come base.
     *             Se è null, allora alcuni tipi di domande
     *             sono esclusi dal generatore.
     */
    public GeneratoreDomanda(DocumentoTestuale primoDocumento, DocumentoTestuale secondoDocumento) {
        if (primoDocumento == null)
            throw new IllegalArgumentException("Il primo documento non può essere null");

        this.primoDocumento = primoDocumento;
        this.secondoDocumento = secondoDocumento;
        random = new Random();
    }

    /**
     * @brief Genera una domanda da porre nel quiz.
     *
     * Utilizza uno o due documenti per generare una Domanda.
     *
     * @return Domanda del quiz.
     * @throws IllegalStateException se non riesce a generare una domanda
     *                               valida entro 100 tentativi randomici.
     */
    public Domanda generaDomanda() {
        Domanda domanda = null;

        // Tenta 100 volte di generare una domanda
        for (int i = 0; domanda == null && i < 100; i++)
            try {
                if (secondoDocumento == null)
                    domanda = switch (random.nextInt(4)) {
                        case 1 -> domandaConfronto(getRandomIndex());
                        case 2 -> domandaEsclusione(getRandomIndex());
                        case 3 -> domandaFrequenzaSpecifica(getRandomIndex());
                        default -> domandaFrequenzaAssoluta(getRandomIndex());
                    };
                else
                    domanda = switch (random.nextInt(6)) {
                        case 1 -> domandaConfronto(getRandomIndex());
                        case 2 -> domandaEsclusione(getRandomIndex());
                        case 3 -> domandaFrequenzaSpecifica(getRandomIndex());
                        case 4 -> domandaDocumentoSpecifico(getRandomIndex());
                        case 5 -> domandaConfrontoFrequenze(getRandomIndex());
                        default -> domandaFrequenzaAssoluta(getRandomIndex());
                    };
            } catch (SQLException e) {
                Logger.warn("Errore durante la generazione " + i + " della domanda: " + e.getMessage());
            }

        // Se non riesce a generare una domanda, solleva l'eccezione
        if (domanda == null)
            throw new IllegalStateException("Impossibile generare una domanda valida");
        return domanda;
    }

    /**
     * @brief Genera una domanda del tipo "frequenza assoluta".
     *
     * Tenta di generare una domanda del tipo:
     * *"Quante volte compare la parola PAROLA nel documento DOCUMENTO?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaFrequenzaAssoluta(int docIndex) throws SQLException {
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
        for (int i = 0; opzioni.size() < 4 && i < 100; i++) {
            int fake = Math.max(0, frequenza + random.nextInt(7) - 3);
            if (fake != frequenza && !opzioni.contains(fake)) opzioni.add(fake);
        }
        if (opzioni.size() < 4)
            return null;
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

    /**
     * @brief Genera una domanda del tipo "frequenza specifica".
     *
     * Tenta di generare una domanda del tipo:
     * *"Quale parola compare esattamente FREQUENZA volte nel documento DOCUMENTO?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaFrequenzaSpecifica(int docIndex) throws SQLException {
        var documento = getDocumento(docIndex);

        // Ottengo tutte le parole del documento con la loro frequenza
        Map<String, Integer> frequenze = documento.getConteggioParole();
        List<String> parole = new ArrayList<>(frequenze.keySet());

        // Scelgo una parola a caso nel documento tra quelle
        // che hanno frequenza maggiore di 1
        List<String> paroleNonUniche = parole.stream().filter(p -> frequenze.get(p) > 1).toList();

        // Se non ci sono parole con frequenza maggiore di 1,
        // annulla la creazione e restituisci null
        if (paroleNonUniche.isEmpty())
            return null;

        String parola = paroleNonUniche.get(random.nextInt(paroleNonUniche.size()));
        int frequenza = frequenze.get(parola);

        // Scelgo quattro opzioni a caso, di cui una corretta
        var opzioni = new ArrayList<String>();
        opzioni.add(parola);
        for (int i = 0; opzioni.size() < 4 && i < 100; i++) {
            String errata = parole.get(random.nextInt(parole.size()));
            if (frequenza != frequenze.get(errata))
                opzioni.add(errata);
        }
        if (opzioni.size() < 4)
            return null;
        Collections.shuffle(opzioni);

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "Quale parola compare esattamente %d volte nel documento \"%s\"?",
            frequenza, documento.getNome()
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            String.valueOf(opzioni.get(0)),
            String.valueOf(opzioni.get(1)),
            String.valueOf(opzioni.get(2)),
            String.valueOf(opzioni.get(3)),
            opzioni.indexOf(parola)
        );
    }

    /**
     * @brief Genera una domanda del tipo "confronto".
     *
     * Tenta di generare una domanda del tipo:
     * *"Quale di queste parole compare più volte nel documento DOCUMENTO?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaConfronto(int docIndex) throws SQLException {
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

        for (int i = 0; opzioni.size() < 4 && i < 100; i++) {
            String parola = parole.get(random.nextInt(parole.size()));
            if (!parola.equals(parolaFrequente) && frequenze.get(parola) < frequenzaMax && !opzioni.contains(parola))
                opzioni.add(parola);
        }
        if (opzioni.size() < 4)
            return null;
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

    /**
     * @brief Genera una domanda del tipo "esclusione".
     *
     * Tenta di generare una domanda del tipo:
     * *"Quale di queste parole non è presente nel documento DOCUMENTO?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaEsclusione(int docIndex) throws SQLException {
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
        for (int i = 0; opzioni.size() < 4 && i < 100; i++) {
            String parola = parole.get(random.nextInt(parole.size()));
            if (!opzioni.contains(parola))
                opzioni.add(parola);
        }
        if (opzioni.size() < 4)
            return null;
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

    /**
     * @brief Genera una domanda del tipo "documento specifico".
     *
     * Tenta di generare una domanda del tipo:
     * *"In quale documento compare la parola PAROLA?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaDocumentoSpecifico(int docIndex) throws SQLException {
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

    /**
     * @brief Genera una domanda del tipo "confronto frequenze".
     *
     * Tenta di generare una domanda del tipo:
     * *"In quale documento compare più volte la parola PAROLA?"*.
     *
     * @param docIndex Il Documento da selezionare per la domanda (`0` o `1`).
     * @return Domanda se riesce a generarne una valida, `null` altrimenti.
     */
    private Domanda domandaConfrontoFrequenze(int docIndex) throws SQLException {
        var documento = getDocumento(docIndex);
        var altro = getDocumento(1 - docIndex);

        // Ottengo la lista delle parole comuni
        List<String> paroleComuni = documento.getParoleComuni(altro);

        // Se non ci sono parole esclusive,
        // annulla la creazione e restituisci null
        if (paroleComuni.isEmpty()) return null;

        // Scelgo una parola a caso tra quelle comuni
        String parola = paroleComuni.get(random.nextInt(paroleComuni.size()));

        // Controllo la frequenza della parola
        // nei due documenti
        int frequenzaPrimo = documento.getConteggioParole().get(parola);
        int frequenzaSecondo = altro.getConteggioParole().get(parola);
        String corretta = "Stessa frequenza";
        if (frequenzaPrimo > frequenzaSecondo)
            corretta = documento.getNome();
        if (frequenzaPrimo < frequenzaSecondo)
            corretta = altro.getNome();

        // Compongo le quattro opzioni
        var opzioni = new ArrayList<String>();
        opzioni.add(documento.getNome());
        opzioni.add(altro.getNome());
        opzioni.add("Stessa frequenza");
        opzioni.add("Solo in " + documento.getNome());

        // Scrivo il testo della domanda
        String testoDomanda = String.format(
            "In quale documento compare più volte la parola \"%s\"?",
            parola
        );

        // Costruisco la domanda
        return new Domanda(
            testoDomanda,
            opzioni.get(0),
            opzioni.get(1),
            opzioni.get(2),
            opzioni.get(3),
            opzioni.indexOf(corretta)
        );
    }

    /** \cond DOXY_SKIP */
    private int getRandomIndex() {
        return random.nextInt(secondoDocumento == null ? 1 : 2);
    }

    private DocumentoTestuale getDocumento(int index) {
        return (index == 0) ? primoDocumento : secondoDocumento;
    }
    /** \endcond */
    
}