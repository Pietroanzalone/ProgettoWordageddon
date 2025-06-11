package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

/**
 * @class HomeController
 * @brief Il controller per la gestione della schermata "Home".
 * @ingroup ui
 *
 * Questa classe gestisce i pulsanti iniziali dell'applicazione
 * che permettono all'utente di accedere o registrarsi.
 * Ogni pulsante porta alla relativa schermata di login o registrazione.
 *
 * \image html Home.png width=80%
 */
public class HomeController extends Controller {

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione.
     * Utilizza il metodo {@link #impostaNavbar()} della classe base.
     */
    @Override
    public void initialize() {
        impostaNavbar();
    }

    /**
     * @brief Metodo chiamato quando si clicca sul pulsante "REGISTRATI".
     *
     * Registra il log dell'interazione e cambia la schermata attuale
     * con la schermata "Registrati".
     * In una fase iniziale, la schermata "Utente" può essere usata come placeholder.
     *
     * @see #cambiaSchermata(String)
     */
    @FXML
    private void registratiClicked() {
        Logger.log("Cliccato il pulsante: REGISTRATI");
        cambiaSchermata("Registrati");
    }

    /**
     * @brief Metodo chiamato quando si clicca sul pulsante "ACCEDI".
     *
     * Registra il log dell'interazione e cambia la schermata attuale
     * con la schermata "Accedi".
     * In una fase iniziale, la schermata "Utente" può essere usata come placeholder.
     *
     * @see #cambiaSchermata(String)
     */
    @FXML
    private void accediClicked() {
        Logger.log("Cliccato il pulsante: ACCEDI");
        cambiaSchermata("Accedi");
    }

}