package com.example.progettowordageddon.ui;

import javafx.fxml.FXML;

public class PannelloControlloController extends Controller {

    @FXML
    public void gestioneStopwordsClicked(){
        cambiaSchermata("GestioneStopwords");
    }

    @FXML
    public void gestioneAdminClicked(){
        cambiaSchermata("GestioneAdmin");
    }

    @FXML
    public void gestioneDocumentiClicked(){
        cambiaSchermata("GestioneDocumenti");
    }

    @FXML public void esciSenzaSalvareClicked(){
        cambiaSchermata("PannelloControllo");
    }
}
