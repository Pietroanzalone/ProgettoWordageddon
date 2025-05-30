package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;

public class AccediController extends Controller {

    @Override
    public void initialize() {
        super.initialize();
        setLogoutIcon();
    }

    public void accediClicked() {
        Logger.log("Cliccato il pulsante: ACCEDI");
        Logger.error("Logica di accesso non implementata");
        cambiaSchermata("Utente");
    }

}