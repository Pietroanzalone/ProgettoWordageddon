package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

public class HomeController extends Controller {

    @FXML
    private void registratiClicked() {
        // In attesa della creazione della schermata
        // "Registrati", questo pulsante porta direttamente
        // alla schermata "Utente"
        Logger.log("Cliccato il pulsante: REGISTRATI");
        cambiaSchermata("Registrati");
    };

    @FXML
    private void accediClicked() {
        // In attesa della creazione della schermata
        // "Accedi", questo pulsante porta direttamente
        // alla schermata "Utente"
        Logger.log("Cliccato il pulsante: ACCEDI");
        cambiaSchermata("Accedi");
    };

    @Override
    public void initialize() {
        initNavbar();
    }

}