package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Sessione;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * @class UtenteController
 * @brief Il controller per la schermata utente autenticato.
 *
 * Gestisce il comportamento della schermata utente, inclusa
 * la visibilità del pannello di controllo e l'accesso alle
 * funzionalità disponibili.
 */
public class UtenteController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private Button B_pannelloDiControllo;

    @FXML
    private Label L_titolo;
    /** \endcond */

    /**
     * @brief Inizializza la schermata utente.
     *
     * Imposta il titolo di benvenuto con il nome utente
     * e determina se mostrare il pulsante del pannello
     * di controllo in base ai privilegi dell'utente.
     */
    @Override
    public void initialize() {
        super.initialize();
        if (Sessione.utente.getUsername() == null) {
            Logger.error("Utente non inizializzato");
            L_titolo.setText("Benvenuto, NULL");
            B_pannelloDiControllo.setVisible(false);
        } else {
            L_titolo.setText("Benvenuto, " + Sessione.utente.getUsername());
            B_pannelloDiControllo.setVisible(Sessione.utente.isAdmin());
        }
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Inizia Quiz".
     *
     * Cambia schermata verso la selezione del quiz.
     */
    @FXML
    private void iniziaQuizClicked() {
        Logger.log("Cliccato il pulsante: INIZIA QUIZ");
        cambiaSchermata("IniziaQuiz");
    }

    @FXML
    private void recuperaSessioneClicked() {
        Logger.log("Cliccato il pulsante: RECUPERA SESSIONE");
        Logger.error("RECUPERA SESSIONE non ancora implementato");
    }

    @FXML
    private void leaderboardClicked() {
        Logger.log("Cliccato il pulsante: LEADERBOARD");
        Logger.error("LEADERBOARD non ancora implementata");
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Pannello di Controllo".
     *
     * Accessibile solo agli amministratori; porta alla schermata di gestione.
     */
    @FXML
    private void pannelloDiControlloClicked() {
        Logger.log("Cliccato il pulsante: PANNELLO DI CONTROLLO");
        cambiaSchermata("PannelloControllo");
    }

}
