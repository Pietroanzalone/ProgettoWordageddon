package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.*;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class VediTestoController extends Controller {

    @FXML
    private TextArea T_testo;

    @FXML
    private Label L_timer;

    private Timeline timer;

    @Override
    public void initialize() {
        super.initialize();
        T_testo.setEditable(false);
        gestisciDifficolta(Sessione.difficolta);
    }

    private void gestisciDifficolta(Difficolta difficolta) {
        switch (difficolta) {
            case FACILE:
                L_timer.setText("00:30");
                startTimer(30);
                break;
            case MEDIA:
                L_timer.setText("01:00");
                startTimer(60);
                break;
            case DIFFICILE:
                L_timer.setText("02:00");
                startTimer(120);
                break;
        }
    }

    private void startTimer(int secondi) {
        var tempo = new AtomicInteger(secondi - 1);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int tempoRimanente = tempo.getAndDecrement();
            int minutiRimanenti = tempoRimanente / 60;
            int secondiRimanenti = tempoRimanente % 60;

            String testoTempoRimasto = String.format("%02d:%02d", minutiRimanenti, secondiRimanenti);
            Logger.log("Tempo rimasto: " + testoTempoRimasto);
            L_timer.setText(testoTempoRimasto);

            if (tempoRimanente < 0) {
                Logger.log("Tempo scaduto");
                cambiaSchermata("Domanda");
                timer.stop();
            }
        }));
        timer.setCycleCount(secondi + 3);
        timer.play();
    }

    @FXML
    private void prontoClicked() {
        Logger.log("Cliccato il pulsante: PRONTO");
        if (timer != null) timer.stop();
        cambiaSchermata("Domanda");
    }

}