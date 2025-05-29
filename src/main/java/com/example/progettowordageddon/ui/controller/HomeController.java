package com.example.progettowordageddon.ui.controller;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

public class HomeController extends Controller {

    @FXML
    private void registratiClicked() {
        // In attesa della creazione della schermata
        // "Registrati", questo pulsante porta direttamente
        // alla schermata "Utente"
        Logger.log("Cliccato il pulsante: REGISTRATI");
        Logger.error("REGISTRATI non ancora implementato");
        cambiaSchermata("Utente");
    };

    @FXML
    private void accediClicked() {
        // In attesa della creazione della schermata
        // "Accedi", questo pulsante porta direttamente
        // alla schermata "Utente"
        Logger.log("Cliccato il pulsante: ACCEDI");
        Logger.error("ACCEDI non ancora implementato");
        cambiaSchermata("Utente");
    };

}