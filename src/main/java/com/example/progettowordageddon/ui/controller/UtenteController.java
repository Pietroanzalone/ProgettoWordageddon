package com.example.progettowordageddon.ui.controller;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.Logger;
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
        L_titolo.setText("Benvenuto, " + Main.utente.getUsername());
        B_pannelloDiControllo.setVisible(Main.utente.isAdmin());
    }

    @FXML
    private void iniziaQuizClicked() {
        Logger.log("Cliccato il pulsante: INIZIA QUIZ");
        Logger.error("INIZIA QUIZ non ancora implementato");
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
        Logger.error("PANNELLO DI CONTROLLO non ancora implementato");
    };

}