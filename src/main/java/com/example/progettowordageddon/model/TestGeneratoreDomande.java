package com.example.progettowordageddon.model;

/**
 * @class TestGeneratoreDomande
 * @brief Classe di test per la generazione di domande a partire da due documenti testuali.
 *
 * Questa classe contiene un metodo `main` che istanzia due documenti testuali in lingua italiana
 * e utilizza il {@link GeneratoreDomanda} per generare una {@link Domanda}.
 * Se la domanda viene generata correttamente, viene stampata a console.
 */
public class TestGeneratoreDomande {

    /**
     * @brief Metodo principale per l'esecuzione del test.
     *
     * Crea due documenti testuali di difficoltà facile in italiano, li fornisce a un
     * {@link GeneratoreDomanda}, genera una domanda, e la stampa se disponibile.
     *
     * @param args Argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        DocumentoTestuale doc1 = new DocumentoTestuale(
                "La mia giornata",
                Lingua.ITALIANO,
                Difficolta.FACILE,
                "Oggi mi sono svegliato presto. Ho fatto colazione con caffè e biscotti. "
                        + "Poi ho portato il cane a fare una passeggiata. Il cane ha giocato molto. "
                        + "Dopo sono tornato a casa, ho studiato e poi ho pranzato con pasta. Il cane dormiva."
        );

        DocumentoTestuale doc2 = new DocumentoTestuale(
                "The cat",
                Lingua.INGLESE,
                Difficolta.FACILE,
                "The cat sleeps on the sofa. Every morning, the cat jumps on the table to look out of the window. "+ "" +
                        "The dog barks, but the cat does not move. The sun enters the room and warms the carpet. " +
                        "Everything is calm and quiet.");

        GeneratoreDomanda generatore = new GeneratoreDomanda(doc1, doc2);
        Domanda domanda = generatore.generaDomanda();

        if (domanda != null) {
            domanda.chiediDomanda();
        } else {
            System.out.println("Nessuna domanda generata.");
        }
    }
}
