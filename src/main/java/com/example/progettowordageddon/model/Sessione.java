package com.example.progettowordageddon.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/// @class Sessione
/// @brief Classe che rappresenta lo stato globale della sessione utente nell'applicazione.
///
/// Questa classe tiene traccia delle informazioni relative alla sessione corrente,
/// come l'utente attivo, lo stato del quiz, la schermata corrente e la difficoltà selezionata.
public class Sessione implements Serializable {
    /// @brief Nome del file in cui è salvata la sessione.
    private String nomeFile;

    private final String creazione;

    /// @brief Costruttore completo di una nuova sessione.
    ///
    /// Imposta tutti gli attributi ai valori di default e genera
    /// un nuovo nome per il file basandosi sul timestamp della generazione.
    public Sessione() {
        creazione = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MMM_yy-HH_mm-ssSSS"));
        schermata = "Home";
        nomeFile = "Sessioni/" + schermata + "-" + creazione + ".wordageddon";
        loggingAttivo = true;
        stream = System.out;
        utente = new Utente(null, null, true, false);
        quizAttivo = null;
    }

    /// @brief Aggiorna il file della sessione.
    ///
    /// Rinomina il file della sessione per riflettere eventuali cambi di schermata,
    /// poi vi inserisce all'interno i dati della sessione corrente.
    /// In caso di errore, stampa un messaggio di errore con il Logger.
    private void salva() {
        try {
            var file = new File(nomeFile);

            if (file.exists()) {
                if (file.renameTo(new File(aggiornaNomeFile())))
                    nomeFile = aggiornaNomeFile();
                else
                    Logger.error("Errore durante la rinominazione del file della sessione: " + nomeFile);
            }

            try (var out = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
                out.writeObject(this);
                Logger.log("Sessione salvata");
                Logger.log("Sessione: " + this);
            }
        } catch (IOException e) {
            Logger.error("Errore durante il salvataggio della sessione: " + e.getMessage());
        }
    }

    /// @brief Metodo di utility per aggiornare il nome del file di sessione.
    ///
    /// Ricostruisce il nome del file in base alla schermata attuale.
    ///
    /// @return Nome del file aggiornato.
    private String aggiornaNomeFile() {
        return "Sessioni/" + schermata + "-" + creazione + ".wordageddon";
    }

    /// @brief Carica una sessione da file `.wordageddon`.
    ///
    /// Aggiorna i valori della sessione corrente in base a quelli contenuti nel file.
    ///
    /// @param pathFile Percorso assoluto del file da cui caricare la sessione.
    /// @throws IOException se il file è danneggiato o non corrisponde all'attuale versione dell'applicazione.
    /// @throws ClassNotFoundException se una delle classi della sessione salvata non è più presente nell'applicazione.
    /// @throws IllegalArgumentException se l'utente della sessione caricata non corrisponde a quello attualmente loggato.
    public void caricaSessione(String pathFile) throws IOException, ClassNotFoundException, IllegalArgumentException {
        Sessione sessione;

        try (var in = new ObjectInputStream(new FileInputStream(pathFile))) {
            sessione = (Sessione) in.readObject();
            sessione.setStream(System.out);
        }

        if (!utente.equals(sessione.getUtente())) {
            throw new IllegalArgumentException("Utente errato");
        }

        setLoggingAttivo(sessione.getLoggingAttivo());
        setStream(sessione.getStream());
        setSchermata(sessione.getSchermata());
        setUtente(sessione.getUtente());
        setQuizAttivo(sessione.getQuizAttivo());

        Logger.log("Sessione caricata");
        Logger.log("Sessione: " + sessione);
    }

    /// @brief Flag che indica se il logging è attivo.
    ///
    /// Se `true`, le operazioni e i messaggi di log saranno abilitati.
    ///
    /// @default{true}
    /// @see Logger
    private boolean loggingAttivo;

    /// @brief Restituisce `true` se il logging è attivo.
    ///
    /// @return {@link loggingAttivo}
    public boolean getLoggingAttivo() {
        return loggingAttivo;
    }

    /// @brief Imposta lo stato del logging.
    ///
    /// @param loggingAttivo `true` per abilitare il logging, `false` per disabilitarlo.
    public void setLoggingAttivo(boolean loggingAttivo) {
        this.loggingAttivo = loggingAttivo;
        salva();
    }

    /// @brief Stream in cui viene stampato il log dell'applicazione.
    ///
    /// @default{System.out}
    /// @see Logger
    private transient PrintStream stream;

    /// @brief Restituisce lo stream in cui viene stampato il log.
    ///
    /// @return {@link stream}
    public PrintStream getStream() {
        return stream;
    }

    /// @brief Imposta lo stream per il logging.
    ///
    /// @param stream Oggetto `PrintStream` dove stampare i log.
    public void setStream(PrintStream stream) {
        this.stream = stream;
        salva();
    }

    /// @brief Nome della schermata attualmente visualizzata.
    ///
    /// @default{"Home"}
    private String schermata;

    /// @brief Restituisce la schermata attualmente visualizzata.
    ///
    /// @return {@link schermata}
    public String getSchermata() {
        return schermata;
    }

    /// @brief Imposta la schermata corrente.
    ///
    /// @param schermata Nome della nuova schermata.
    public void setSchermata(String schermata) {
        this.schermata = schermata;
        salva();
    }

    /// @brief Utente attualmente loggato nella sessione.
    ///
    /// @default{Utente(null, null, true, false)}
    private Utente utente;

    /// @brief Restituisce l'utente attualmente loggato.
    ///
    /// @return {@link utente}
    public Utente getUtente() {
        return utente;
    }

    /// @brief Imposta l'utente corrente.
    ///
    /// @param utente Oggetto `Utente` che rappresenta l'utente attivo.
    public void setUtente(Utente utente) {
        this.utente = utente;
        salva();
    }

    /// @brief Quiz attualmente in corso.
    ///
    /// @default{null}
    public Quiz quizAttivo;

    /// @brief Restituisce il quiz attivo.
    ///
    /// @return {@link quizAttivo}
    public Quiz getQuizAttivo() {
        return quizAttivo;
    }

    /// @brief Imposta il quiz attivo nella sessione.
    ///
    /// @param quizAttivo Oggetto `Quiz` attualmente in corso.
    public void setQuizAttivo(Quiz quizAttivo) {
        this.quizAttivo = quizAttivo;
        salva();
    }

    /// @brief Rappresentazione testuale della sessione.
    ///
    /// @return Stringa contenente informazioni sullo stato della sessione.
    @Override
    public String toString() {
        return nomeFile + "\n"
                + "loggingAttivo = " + loggingAttivo + "\n"
                + "utente = " + utente + "\n"
                + "schermata = " + schermata + "\n"
                + "quizAttivo = " + (quizAttivo != null);
    }

}