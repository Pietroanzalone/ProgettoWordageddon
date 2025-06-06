package com.example.progettowordageddon.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    /** \cond DOXY_SKIP */
    private static final String RESET = "\u001B[0m";
    private static final String ROSSO = "\u001B[31m";
    private static final String ROSSOGRASSETTO = "\u001B[1;31m";
    private static final String GIALLO = "\u001B[33m";
    private static final String BLU = "\u001B[34m";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /** \endcond */

    /**
     * @brief Stampa un messaggio informativo in blu.
     *
     * @param messaggio Il messaggio da stampare.
     *
     * @note Da usare per messaggi di stato generali, informazioni sul normale funzionamento 
     *       dell'applicazione (es. "File caricato", "Utente loggato").
     */
    public static void log(String messaggio) {
        if (!Sessione.loggingAttivo) return;
        Sessione.stream.println(RESET + "[" + BLU + "INFO" + RESET + "]  [" + timestamp() + "] " + messaggio);
    }

    /**
     * @brief Stampa un messaggio di warning (avviso) in giallo.
     *
     * @param messaggio Il messaggio di avviso.
     *
     * @note Da usare per situazioni anomale ma non critiche (es. "Configurazione mancante, uso valori di default").
     */
    public static void warn(String messaggio) {
        if (!Sessione.loggingAttivo) return;
        Sessione.stream.println(RESET + "[" + GIALLO + "WARN" + RESET + "]  [" + timestamp() + "] " + messaggio);
    }

    /**
     * @brief Stampa un messaggio di errore in rosso.
     *
     * @param messaggio Il messaggio di errore.
     *
     * @note Da usare quando si verifica un errore recuperabile o gestibile,
     *       ma che indica comunque un'anomalia (es. "File non trovato").
     */
    public static void error(String messaggio) {
        if (!Sessione.loggingAttivo) return;
        Sessione.stream.println(RESET + "[" + ROSSO + "ERROR" + RESET + "] [" + timestamp() + "] " + messaggio);
    }

    /**
     * @brief Stampa un messaggio di errore critico (fatale) in rosso.
     *
     * @param messaggio Il messaggio critico.
     *
     * @note Da usare in casi gravi che impediscono il proseguimento del programma,
     *       come errori non gestibili o blocchi totali (es. "Database non raggiungibile").
     */
    public static void fatal(String messaggio) {
        if (!Sessione.loggingAttivo) return;
        Sessione.stream.println(RESET + "[" + ROSSOGRASSETTO + "FATAL" + RESET + "] [" + timestamp() + "] " + messaggio);
    }

    /** \cond DOXY_SKIP */
    private static String timestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
    /** \endcond */

}
