package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class IniziaQuizController extends Controller {
    private Lingua linguaScelta = Lingua.ITALIANO;
    private Difficolta difficoltaScelta = Difficolta.FACILE;

    @FXML
    private MenuButton B_difficolta;

    @FXML
    private MenuButton B_lingua;

    @FXML
    private MenuItem DIF_facile, DIF_media, DIF_difficile;

    @FXML
    private MenuItem LIN_italiano, LIN_inglese, LIN_spagnolo, LIN_francese;

    @Override
    public void initialize() {
        super.initialize();
        initDifficolta();
        initLingua();
    }

    private void initDifficolta() {
        styleDifficolta();
        DIF_facile.setOnAction(e -> {
            difficoltaScelta = Difficolta.FACILE;
            Logger.log("Difficolta scelta: FACILE");
            B_difficolta.setText("Facile");
            styleDifficolta();
        });
        DIF_media.setOnAction(e -> {
            difficoltaScelta = Difficolta.MEDIA;
            Logger.log("Difficolta scelta: MEDIA");
            B_difficolta.setText("Media");
            styleDifficolta();
        });
        DIF_difficile.setOnAction(e -> {
            difficoltaScelta = Difficolta.DIFFICILE;
            Logger.log("Difficolta scelta: DIFFICILE");
            B_difficolta.setText("Difficile");
            styleDifficolta();
        });
    }

    private void initLingua() {
        styleLingua();
        LIN_italiano.setOnAction(e -> {
            linguaScelta = Lingua.ITALIANO;
            Logger.log("Lingua scelta: ITALIANO");
            styleLingua();
        });
        LIN_inglese.setOnAction(e -> {
            linguaScelta = Lingua.INGLESE;
            Logger.log("Lingua scelta: INGLESE");
            styleLingua();
        });
        LIN_francese.setOnAction(e -> {
            linguaScelta = Lingua.FRANCESE;
            Logger.log("Lingua scelta: FRANCESE");
            styleLingua();
        });
        LIN_spagnolo.setOnAction(e -> {
            linguaScelta = Lingua.SPAGNOLO;
            Logger.log("Lingua scelta: SPAGNOLO");
            styleLingua();
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

    private void styleLingua() {
        B_lingua.setGraphic(caricaImmagine(linguaScelta.toString() + ".png", 130, 70));
        B_lingua.setText("");
        B_lingua.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @FXML
    private void iniziaClicked() {
        Logger.log("Cliccato il pulsante: INIZIA");
        Logger.log("Difficolta scelta: " + difficoltaScelta.name());
        Logger.log("Lingua scelta: " + linguaScelta);
        Sessione.quizAttivo = true;
        Sessione.difficolta = difficoltaScelta;
        cambiaSchermata("VediTesto");
    }

}
