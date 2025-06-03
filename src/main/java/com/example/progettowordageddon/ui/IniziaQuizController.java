package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Sessione;
import com.example.progettowordageddon.model.Utente;
import javafx.fxml.FXML;import javafx.scene.control.MenuButton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;


public class IniziaQuizController extends Controller {

    @FXML
    private MenuButton B_SelettoreDifficolta;

    @FXML
    private MenuButton B_SelettoreLingua;


    @FXML
    private MenuItem facile;

    @FXML
    private MenuItem media;

    @FXML
    private MenuItem difficile;

    @FXML
    private MenuItem italiano;

    @FXML
    private MenuItem inglese;

    @FXML
    private MenuItem spagnolo;

    @FXML
    private MenuItem francese;

    @Override
    public void initialize() {
        super.initialize();
        setLogoutIcon();

        facile.setOnAction(e -> B_SelettoreDifficolta.setText("Facile"));
        media.setOnAction(e -> B_SelettoreDifficolta.setText("Media"));
        difficile.setOnAction(e -> B_SelettoreDifficolta.setText("Difficile"));

        italiano.setOnAction(e-> B_SelettoreLingua.setText("Italiano"));
        francese.setOnAction(e-> B_SelettoreLingua.setText("Francese"));
        inglese.setOnAction(e-> B_SelettoreLingua.setText("Inglese"));
        spagnolo.setOnAction(e-> B_SelettoreLingua.setText("Spagnolo"));

    }

    @FXML
    private void homeUtente() {
        cambiaSchermata("Utente");
    }

    @FXML
    private void IniziaClicked(){
        Logger.log("Cliccato il pulsante: INIZIA ");
        Logger.error("QUIZ non ancora implementato");
        cambiaSchermata("Quiz");
    }
}
