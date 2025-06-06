package com.example.progettowordageddon.model;

import java.io.PrintStream;

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
     *
     * Se `true`, le operazioni e i messaggi di log saranno abilitati.
     *
     * @default{true}
     *
     * @see Logger
     */
    public static boolean loggingAttivo;

    /** \cond DOXY_SKIP */
    static {
        loggingAttivo = true;
    }
    /** \endcond */

    /**
     * @brief Stream in cui viene stampato il log dell'applicazione.
     *
     * @default{System.out}
     *
     * @see Logger
     */
    public static PrintStream stream;

    /** \cond DOXY_SKIP */
    static {
        stream = System.out;
    }
    /** \endcond */

    /**
     * @brief Nome della schermata attualmente visualizzata.
     *
     * @default{"Home"}
     */
    public static String schermata;

    /** \cond DOXY_SKIP */
    static {
        schermata = "Home";
    }
    /** \endcond */

    /**
     * @brief Utente attualmente loggato nella sessione.
     *
     * @default{Utente(null, null, true, false)}
     */
    public static Utente utente;

    /** \cond DOXY_SKIP */
    static {
        utente = new Utente(null, null, true, false);
    }
    /** \endcond */

    /**
     * @brief Indica se un quiz è attualmente in corso.
     *
     * @default{false}
     */
    public static boolean quizAttivo;

    /** \cond DOXY_SKIP */
    static {
        quizAttivo = false;
    }
    /** \endcond */

    /**
     * @brief Difficoltà selezionata per il quiz o l'attività corrente.
     */
    public static Difficolta difficolta;

}