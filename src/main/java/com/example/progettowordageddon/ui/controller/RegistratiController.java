package com.example.progettowordageddon.ui.controller;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

public class RegistratiController extends Controller {

    @Override
    public void initialize() {
        super.initialize();
        setLogoutIcon();
    }

    @FXML
    public void registratiClicked() {
        Logger.log("Cliccato il pulsante: REGISTRATI");
        Logger.error("Logica di accesso non implementata");
        cambiaSchermata("Utente");
    }

}