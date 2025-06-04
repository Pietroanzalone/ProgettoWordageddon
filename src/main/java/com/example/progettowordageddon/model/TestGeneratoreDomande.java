package com.example.progettowordageddon.model;

public class TestGeneratoreDomande {
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
                "L'impero Romano",
                Lingua.ITALIANO,
                Difficolta.FACILE,
                "Oggi mi sono svegliato presto. Ho pensato presto alla grandezze dell'impero romano "
                        + "Poi ho parlato a mio figlio dell'impero romano. Mio figlio ha apprezzato molto. "
                        + "Mio figlio ha detto che voleva diventare anche lui un romano, mio figlio non sa che l'impero è finito"
        );

        GeneratoreDomanda generatore = new GeneratoreDomanda(doc1, doc2);
        Domanda domanda = generatore.generaDomanda();

        if (domanda != null) {
            domanda.chiediDomanda();
        } else {
            System.out.println("Nessuna domanda generata.");
        }
    }
}

