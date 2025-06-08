package com.example.progettowordageddon.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @class Sessione
 * @brief Classe statica che rappresenta lo stato globale della sessione utente nell'applicazione.
 *
 * Questa classe tiene traccia delle informazioni relative alla sessione corrente,
 * come l'utente attivo, lo stato del quiz, la schermata corrente e la difficoltà selezionata.
 */
public class Sessione implements Serializable {
    private final String nomeFile;

    public Sessione() {
        nomeFile = "Sessioni/Sessione-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy-HH-mm-ss-SSS")) + ".wordageddon";
        loggingAttivo = true;
        stream = System.out;
        schermata = "Home";
        utente = new Utente(null, null, true, false);
        quizAttivo = null;
    }

    private void salva() {
        try (var out = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
            out.writeObject(this);
            Logger.log("Sessione salvata");
            Logger.log("Sessione : " + this);
        } catch (IOException e) {
            Logger.error("Errore durante il salvataggio della sessione: " + e.getMessage());
        }
    }

    public void caricaSessione(String pathFile) throws IOException, ClassNotFoundException {
        var sessione = fromFile(pathFile);

        setLoggingAttivo(sessione.getLoggingAttivo());
        setStream(sessione.getStream());
        setSchermata(sessione.getSchermata());
        setUtente(sessione.getUtente());
        setQuizAttivo(sessione.getQuizAttivo());
    }

    public static Sessione fromFile(String pathFile) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(new FileInputStream(pathFile))) {
            var sessione = (Sessione) in.readObject();
            sessione.setStream(System.out);
            return sessione;
        }
    }

    /**
     * @brief Flag che indica se il logging è attivo.
     *
     * Se `true`, le operazioni e i messaggi di log saranno abilitati.
     *
     * @default{true}
     *
     * @see Logger
     */
    private boolean loggingAttivo;

    public boolean getLoggingAttivo() {
        return loggingAttivo;
    }

    public void setLoggingAttivo(boolean loggingAttivo) {
        this.loggingAttivo = loggingAttivo;
        salva();
    }

    /**
     * @brief Stream in cui viene stampato il log dell'applicazione.
     *
     * @default{System.out}
     *
     * @see Logger
     */
    private transient PrintStream stream;

    public PrintStream getStream() {
        return stream;
    }

    public void setStream(PrintStream stream) {
        this.stream = stream;
        salva();
    }

    /**
     * @brief Nome della schermata attualmente visualizzata.
     *
     * @default{"Home"}
     */
    private String schermata;

    public String getSchermata() {
        return schermata;
    }

    public void setSchermata(String schermata) {
        this.schermata = schermata;
        salva();
    }

    /**
     * @brief Utente attualmente loggato nella sessione.
     *
     * @default{Utente(null, null, true, false)}
     */
    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
        salva();
    }

    /**
     * @brief Quiz attualmente in corso.
     *
     * @default{null}
     */
    public Quiz quizAttivo;

    public Quiz getQuizAttivo() {
        return quizAttivo;
    }

    public void setQuizAttivo(Quiz quizAttivo) {
        this.quizAttivo = quizAttivo;
        salva();
    }

    @Override
    public String toString() {
        return nomeFile + "\n"
            + "loggingAttivo = " + loggingAttivo + "\n"
            + "utente = " + utente + "\n"
            + "schermata = " + schermata + "\n"
            + "quizAttivo = " + (quizAttivo != null);
    }

}