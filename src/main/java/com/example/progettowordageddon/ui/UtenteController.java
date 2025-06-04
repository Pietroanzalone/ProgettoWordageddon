package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Sessione;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UtenteController extends Controller {

    @FXML
    private Button B_pannelloDiControllo;

    @FXML
    private Label L_titolo;

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

    @FXML
    private void iniziaQuizClicked() {
        Logger.log("Cliccato il pulsante: INIZIA QUIZ");
        cambiaSchermata("IniziaQuiz");
    };

    @FXML
    private void recuperaSessioneClicked() {
        Logger.log("Cliccato il pulsante: RECUPERA SESSIONE");
        Logger.error("RECUPERA SESSIONE non ancora implementato");
    };

    @FXML
    private void leaderboardClicked() {
        Logger.log("Cliccato il pulsante: LEADERBOARD");
        Logger.error("LEADERBOARD non ancora implementata");
    };

    @FXML
    private void pannelloDiControlloClicked() {
        Logger.log("Cliccato il pulsante: PANNELLO DI CONTROLLO");
        cambiaSchermata("PannelloControllo");
    };

}