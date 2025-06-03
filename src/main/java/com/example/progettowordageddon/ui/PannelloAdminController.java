package com.example.progettowordageddon.ui;

import javafx.fxml.FXML;

public class PannelloAdminController extends Controller {

    @Override
    public void initialize() {
        super.initialize();
        setLogoutIcon();
    }

    @FXML
    public void gestioneStopWordsClicked(){
        cambiaSchermata("StopwordsPanel");
    }

    @FXML
    public void gestioneAdminClicked(){
        cambiaSchermata("AdminPanel");
    }

    @FXML
    public void gestioneDocumentiClicked(){
        cambiaSchermata("DocumentiPanel");
    }

    @FXML public void esciSenzaSalvareClicked(){
        cambiaSchermata("ControlPanel");
    }
}
