package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;

public class PannelloControlloController extends Controller {

    @FXML
    public void gestioneStopwordsClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE STOPWORDS");
        cambiaSchermata("GestioneStopwords");
    }

    @FXML
    public void gestioneAdminClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE ADMIN");
        cambiaSchermata("GestioneAdmin");
    }

    @FXML
    public void gestioneDocumentiClicked() {
        Logger.log("Cliccato il pulsante: GESTIONE DOCUMENTI");
        cambiaSchermata("GestioneDocumenti");
    }

}