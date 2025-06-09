package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * @class UtenteController
 * @brief Il controller per la gestione della schermata "Utente".
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
        if (Main.sessione.getUtente().getUsername() == null) {
            Logger.error("Utente non inizializzato");
            L_titolo.setText("Benvenuto, NULL");
            B_pannelloDiControllo.setVisible(false);
        } else {
            L_titolo.setText("Benvenuto, " + Main.sessione.getUtente().getUsername());
            B_pannelloDiControllo.setVisible(Main.sessione.getUtente().isAdmin());
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

        var fileChooser = new FileChooser();
        fileChooser.setTitle("Recupera sessione");
        fileChooser.setInitialDirectory(new File("Sessioni"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sessione", "*.wordageddon"));
        File selezionato = fileChooser.showOpenDialog(L_titolo.getScene().getWindow());
        if (selezionato == null) {
            Logger.warn("Nessun file selezionato");
            return;
        }

        try {
            Main.sessione.caricaSessione(selezionato.getAbsolutePath());
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Impossibile caricare la sessione " + selezionato.getName());
            mostraErrore(
                "Impossibile caricare la sessione",
                "Questa sessione è danneggiata o corrisponde a una versione precedente dell'applicazione"
            ).showAndWait();
            return;
        }
        cambiaSchermata(Main.sessione.getSchermata());
    }

    @FXML
    private void leaderboardClicked() {
        Logger.log("Cliccato il pulsante: LEADERBOARD");
        cambiaSchermata("Leaderboard");
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
