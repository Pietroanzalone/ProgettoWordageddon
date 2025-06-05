package com.example.progettowordageddon.model;

import com.example.progettowordageddon.database.StopwordsDAO;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentoTestuale {
    private String nome;
    private Lingua lingua;
    private Difficolta difficolta;
    private String testo;
    private final Map<String, Integer> conteggioParole;

    public DocumentoTestuale(String nome, Lingua lingua, Difficolta difficolta, String testo) {
        this.nome = nome;
        this.lingua = lingua;
        this.difficolta = difficolta;
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

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
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
            if (!StopwordsDAO.contiene(parola))
                conteggioParole.put(parola, conteggioParole.getOrDefault(parola, 0) + 1);
        }
    }

    @Override
    public String toString() {
        return "\"" + nome + "\" [" + lingua + "] [" + difficolta + "]";
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

}