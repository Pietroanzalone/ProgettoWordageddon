package com.example.progettowordageddon.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class QuizController extends Controller {

    @FXML
    private TextArea QuizTextArea;

    @FXML
    private Label QuizTimer;

    @Override
    public void initialize(){
        QuizTextArea.setEditable(false);

    }

    @FXML
    private void gestisciDifficolta(String difficolta){
        switch(difficolta){
            case "Facile":
                QuizTimer.setText("00:30");
                startTimer(30);
                break;
            case "Media":
                QuizTimer.setText("01:00");
                startTimer(60);
                break;
            case "Difficile":
                QuizTimer.setText("02:00");
                startTimer(120);
                break;
        }
    }

    public void startTimer(int secondi ){
        AtomicInteger tempo = new AtomicInteger(secondi);
         Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int rimanenti = tempo.getAndDecrement();
             int minuti = rimanenti / 60;
             int secondiRimasti = rimanenti % 60;

             QuizTimer.setText(String.format("%02d:%02d", minuti, secondiRimasti));
        }));
        timeline.setCycleCount(secondi);
        timeline.play();
    }
}
