package com.example.progettowordageddon.model;

/**
 * @class Sessione
 * @brief Classe statica che rappresenta lo stato globale della sessione utente nell'applicazione.
 *
 * Questa classe tiene traccia delle informazioni relative alla sessione corrente,
 * come l'utente attivo, lo stato del quiz, la schermata corrente e la difficoltà selezionata.
 */
public class Sessione {

    /**
     * @brief Flag che indica se il logging è attivo.
     * @details Se true, le operazioni e i messaggi di log saranno abilitati.
     */
    public static boolean loggingAttivo = true;

    /**
     * @brief Nome della schermata attualmente visualizzata.
     * @details Valore predefinito: "Home".
     */
    public static String schermata = "Home";

    /**
     * @brief Utente attualmente loggato nella sessione.
     * @details Viene inizializzato con un utente vuoto (null username e password).
     */
    public static Utente utente = new Utente(null, null, true, false);

    /**
     * @brief Indica se un quiz è attualmente in corso.
     */
    public static boolean quizAttivo = false;

    /**
     * @brief Difficoltà selezionata per il quiz o l'attività corrente.
     */
    public static Difficolta difficolta;
}
