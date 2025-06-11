package com.example.progettowordageddon.model;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import com.example.progettowordageddon.database.StopwordsDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

///  @class DocumentoTestuale
///  @brief Rappresenta un documento testuale associato a una lingua, difficoltà e testo contenuto.
///  @ingroup model
///  Include funzionalità per analisi testuale come conteggio parole, parole comuni, e parole più frequenti.
public class DocumentoTestuale implements Serializable, Comparable<DocumentoTestuale> {
    /// @brief Nome identificativo del documento.
    private String nome;

    /// @brief L%ingua del documento.
    private Lingua lingua;

    /// @brief Difficoltà del documento.
    private Difficolta difficolta;

    /// @brief Testo contenuto nel documento.
    private String testo;

    /// @brief Mappa delle parole e delle rispettive frequenze.
    private final Map<String, Integer> conteggioParole;

    /// @brief Costruttore del documento testuale.
    /// @param nome Nome del documento.
    /// @param lingua Lingua del documento.
    /// @param difficolta Difficoltà del documento.
    /// @param testo Testo da analizzare.
    public DocumentoTestuale(String nome, Lingua lingua, Difficolta difficolta, String testo) {
        this.nome = nome;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.testo = testo;
        conteggioParole = new HashMap<>();
        aggiornaConteggioParole();
    }

    /// @brief Restituisce il nome del documento.
    /// @return Il nome del documento.
    public String getNome() {
        return nome;
    }

    /// @brief Aggiorna il nome del documento.
    /// @param nome Il nome del documento.
    public void setNome(String nome) throws SQLException {
        DocumentiTestualiDAO.aggiornaNome(this.nome, nome);
        this.nome = nome;
    }

    /// @brief Restituisce la lingua del documento.
    /// @return La lingua del documento.
    public Lingua getLingua() {
        return lingua;
    }

    /// @brief Aggiorna la lingua del documento.
    /// @param lingua La lingua del documento.
    public void setLingua(Lingua lingua) throws SQLException {
        DocumentiTestualiDAO.aggiornaLingua(nome, lingua);
        this.lingua = lingua;
    }

    /// @brief Restituisce la difficoltà del documento.
    /// @return La difficoltà del documento.
    public Difficolta getDifficolta() { return difficolta; }

    /// @brief Aggiorna la difficoltà del documento.
    /// @param difficolta La difficoltà del documento.
    public void setDifficolta(Difficolta difficolta) throws SQLException {
        DocumentiTestualiDAO.aggiornaDifficolta(nome, difficolta);
        this.difficolta = difficolta;
    }

    /// @brief Restituisce il testo del documento.
    /// @return Il testo del documento.
    public String getTesto() throws SQLException {
        if (testo.isBlank())
            testo = DocumentiTestualiDAO.get(nome).getTesto();
        return testo;
    }

    /// @brief Imposta un nuovo testo e aggiorna il conteggio delle parole.
    /// @param testo Nuovo testo del documento.
    public void setTesto(String testo) throws SQLException {
        DocumentiTestualiDAO.aggiornaTesto(nome, testo);
        this.testo = testo;
        aggiornaConteggioParole();
    }

    /// @brief Restituisce una copia del conteggio delle parole.
    /// @return Mappa contenente parole e conteggi.
    public Map<String, Integer> getConteggioParole() throws SQLException {
        if (testo.isBlank())
            setTesto(getTesto());
        return new HashMap<>(conteggioParole);
    }

    /// @brief Verifica se una parola è contenuta nel documento.
    /// @param parola Parola da cercare.
    /// @return True se presente, false altrimenti.
    public boolean contiene(String parola) throws SQLException {
        if (testo.isBlank())
            setTesto(getTesto());
        return conteggioParole.containsKey(parola);
    }

    /// @brief Trova le parole comuni tra questo documento e un altro.
    /// @param altro Altro documento da confrontare.
    /// @return Lista di parole comuni.
    public List<String> getParoleComuni(DocumentoTestuale altro) throws SQLException {
        if (testo.isBlank())
            setTesto(getTesto());
        if (altro == null)
            return new ArrayList<>();
        Set<String> comuniSet = new HashSet<>(conteggioParole.keySet());
        comuniSet.retainAll(altro.getConteggioParole().keySet());
        return new ArrayList<>(comuniSet);
    }

    /// @brief Restituisce le parole più frequenti del documento.
    /// @return Lista di parole con il massimo conteggio.
    public List<String> getParolePiuFrequenti() throws SQLException {
        if (testo.isBlank())
            setTesto(getTesto());
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

    /// @brief Aggiorna il conteggio delle parole ignorando le stopword.
    private void aggiornaConteggioParole() {
        conteggioParole.clear();
        if (testo == null || testo.isEmpty())
            return;
        Pattern pattern = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(testo.toLowerCase());
        while (matcher.find()) {
            String parola = matcher.group();
            if (!StopwordsDAO.contiene(parola))
                conteggioParole.put(parola, conteggioParole.getOrDefault(parola, 0) + 1);
        }
    }

    /// @cond DOXY_SKIP
    @Override
    public String toString() {
        return "\"" + nome + "\" [" + lingua + "] [" + difficolta + "]";
    }
    /// @endcond

    /// @brief Confronta due documenti sulla base del nome.
    /// @param obj Oggetto da confrontare.
    /// @return `true` se hanno lo stesso nome, `false` altrimenti.
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DocumentoTestuale dt)
            return nome.equals(dt.nome);
        return false;
    }

    /// @cond DOXY_SKIP
    @Override
    public int hashCode() {
        return nome.hashCode();
    }
    /// @endcond

    /// @brief Confronta due documenti per l'ordinamento.
    ///
    /// La logica di ordinamento è definita per:
    /// 1. L%ingua
    /// 2. Difficoltà (da `FACILE` a `DIFFICILE`)
    /// 3. Nome (ordine alfabetico)
    ///
    /// @param documento Documento da confrontare
    /// @return Un valore negativo se questo documento precede
    ///         l'altro, un valore positivo se lo segue, `0`
    ///         se sono uguali.
    @Override
    public int compareTo(DocumentoTestuale documento) {
        if (!lingua.equals(documento.getLingua()))
            return lingua.compareTo(documento.getLingua());
        if (!difficolta.equals(documento.getDifficolta()))
            return difficolta.compareTo(documento.getDifficolta());

        return nome.compareTo(documento.getNome());
    }
}
