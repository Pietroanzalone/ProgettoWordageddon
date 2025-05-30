package com.example.progettowordageddon.model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentoTestuale {
    private String nome;
    private Lingua lingua;
    private String testo;
    private final Map<String, Integer> conteggioParole;

    public DocumentoTestuale(String nome, Lingua lingua, String testo) {
        this.nome = nome;
        this.lingua = lingua;
        this.testo = testo;
        conteggioParole = new HashMap<>();
        aggiornaConteggioParole();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
        aggiornaConteggioParole();
    }

    public Map<String, Integer> getConteggioParole() {
        return new HashMap<>(conteggioParole);
    }

    public boolean contiene(String parola) {
        return conteggioParole.containsKey(parola);
    }

    public int getConteggioParola(String parola) {
        if (contiene(parola))
            return conteggioParole.get(parola);
        return 0;
    }

    public List<String> getParoleComuni(DocumentoTestuale altro) {
        if (altro == null) return new ArrayList<>();
        Set<String> comuniSet = new HashSet<>(conteggioParole.keySet());
        comuniSet.retainAll(altro.getConteggioParole().keySet());
        return new ArrayList<>(comuniSet);
    }

    public List<String> getParolePiuFrequenti() {
        List<String> parolePiuFrequenti = new ArrayList<>();
        int maxConteggio = 0;

        for (Map.Entry<String, Integer> entry : conteggioParole.entrySet()) {
            String parola = entry.getKey();
            int conteggio = entry.getValue();
            if (conteggio == maxConteggio)
                parolePiuFrequenti.add(parola);
            if (conteggio > maxConteggio) {
                parolePiuFrequenti.clear();
                parolePiuFrequenti.add(parola);
                maxConteggio = conteggio;
            }
        }

        return parolePiuFrequenti;
    }

    private void aggiornaConteggioParole() {
        conteggioParole.clear();
        if (testo == null || testo.isEmpty()) return;
        Pattern pattern = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(testo.toLowerCase());
        while (matcher.find()) {
            String parola = matcher.group();
            if (!Stopwords.contiene(parola))
                conteggioParole.put(parola, conteggioParole.getOrDefault(parola, 0) + 1);
        }
    }

    @Override
    public String toString() {
        return "\"" + nome + "\" [" + lingua.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DocumentoTestuale dt)
            return nome.equals(dt.nome);
        return false;
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    public static void main(String[] args) {
        String testo = "Omero era un bel gatto dal pelo lungo e folto, lucido come seta. " +
                        "I suoi occhi erano dorati, fondi come pozzi e pieni di saggezza, e" +
                        " i suoi baffi lunghi e luccicanti. Aveva un graziosissimo naso rosa" +
                        " e orecchie a punta. In effetti, Omero aveva un solo difetto, ed " +
                        "era il suo colore. In verità, lui non ci trovava niente di strano," +
                        " ma gli umani, chissà perché, ogni volta che lo vedevano cambiavano" +
                        " strada, o facevano strani gesti, e addirittura qualcuno aveva " +
                        "provato a tirargli dietro degli oggetti. Il suo pelo, infatti, " +
                        "era completamente nero, dalla punta della coda alla punta del " +
                        "musetto. Quando aveva chiesto in giro, alle sue amiche colombe o " +
                        "ai suoi amici cani, per quale motivo i bipedi temessero un gatto " +
                        "nero, bhe, nessuno aveva saputo spiegarglielo, e alla fine si era " +
                        "rassegnato ad essere scansato da tutti. C’era solo una bambina che " +
                        "sembrava non avere nessun pregiudizio verso di lui, e si chiamava " +
                        "Agnese. Agnese era molto bella: aveva lunghi boccoli biondi e grandi " +
                        "occhi color malva. Però era anche molto gracile e delicata; avrebbe " +
                        "avuto bisogno di cure e di mangiare bene, ma la sua famiglia era " +
                        "povera e non poteva darle più di quanto non facesse. Omero si era " +
                        "affezionato alla piccola che ogni giorno, andando a scuola, gli dava " +
                        "qualcosa della sua merenda, nonostante gli amichetti le dicessero di " +
                        "stare alla larga da quella bestiaccia, che portava sfortuna. Poi un " +
                        "giorno Agnese non andò a scuola, e neanche il successivo, e neanche " +
                        "quello dopo. Omero iniziò a preoccuparsi, e decise di cercarla. Girò " +
                        "la città in lungo e in largo: chiese alle tortore, che scossero il " +
                        "capino; chiese alle lucertole, che sibilarono con la loro piccola " +
                        "lingua; chiese finanche alle minuscole formiche, che alzarono le " +
                        "zampette. Infine, mentre cacciava nei pressi del porto, catturò un " +
                        "topolino. Lo prese tra i polpastrelli, e la creaturina squittì " +
                        "impaurita. <<Se non mi uccidi>> disse <<Ti indicherò dove vive la " +
                        "ragazzina che cerchi tanto!>> Tutto contento, Omero lo liberò e si " +
                        "fece condurre dove viveva Agnese...";
        DocumentoTestuale dt = new DocumentoTestuale("Favola", Lingua.ITALIANO, testo);
        System.out.println("Documento: " + dt);
        System.out.println(dt.getTesto());
        dt.getConteggioParole().forEach((s, integer) -> System.out.println(s + " " + integer));
    }

}