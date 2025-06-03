package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;


public class IniziaQuizController extends Controller {

    @FXML
    private MenuButton B_difficolta;

    @FXML
    private MenuButton B_lingua;

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
        initDifficolta();

        italiano.setOnAction(e-> B_lingua.setText("Italiano"));
        francese.setOnAction(e-> B_lingua.setText("Francese"));
        inglese.setOnAction(e-> B_lingua.setText("Inglese"));
        spagnolo.setOnAction(e-> B_lingua.setText("Spagnolo"));

    }

    private void initDifficolta() {
        styleDifficolta();
        facile.setOnAction(e -> {
            B_difficolta.setText("Facile");
            styleDifficolta();
        });
        media.setOnAction(e -> {
            B_difficolta.setText("Media");
            styleDifficolta();
        });
        difficile.setOnAction(e -> {
            B_difficolta.setText("Difficile");
            styleDifficolta();
        });
    }

    private void styleDifficolta() {
        String color = "rgba(255, 255, 255, 0.5)";
        if ("Facile".equals(B_difficolta.getText()))
            color = "rgba(50,205,50,0.5)";
        else if ("Media".equals(B_difficolta.getText()))
            color = "rgba(223,223,93,0.5)";
        else if ("Difficile".equals(B_difficolta.getText()))
            color = "rgba(248,168,150,0.5)";
        B_difficolta.setStyle("-fx-background-color: " + color + ";");
    }

    @FXML
    private void iniziaClicked() {
        Logger.log("Cliccato il pulsante: INIZIA");
        Logger.log("Difficolta scelta: " + B_difficolta.getText());
        cambiaSchermata("Quiz");
    }
}
