package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.Domanda;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RiepilogoQuizController extends Controller {

    @FXML
    private ListView<Domanda> LV_riepilogo;

    @Override
    public void initialize() {
        super.initialize();

        caricaDati();
        impostaVisualeCella();
    }

    private void caricaDati() {
        var quiz = Main.sessione.getQuizAttivo();
        for (var domanda : quiz.getDomande())
            LV_riepilogo.getItems().add(domanda);
    }

    private void impostaVisualeCella() {
        LV_riepilogo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(final Domanda item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setGraphic(null);
                else {
                    Label L_domanda = new Label(item.getTestoDomanda());
                    L_domanda.getStyleClass().add("testo-domanda");
                    Label L_corretta = new Label(item.getCorretta());
                    L_corretta.getStyleClass().add("risposta-corretta");
                    Label L_selezionata = new Label(item.getSelezionata());
                    L_selezionata.getStyleClass().add("risposta-selezionata");
                    Label L_numeroDomanda = new Label(String.valueOf(getIndex() + 1));
                    L_numeroDomanda.getStyleClass().add("numero-domanda");

                    if (L_corretta.getText().equals(L_selezionata.getText()))
                        L_selezionata.setStyle("-fx-text-fill: green;");
                    else
                        L_selezionata.setStyle("-fx-text-fill: red;");

                    HBox risposte = new HBox(5, L_corretta, L_selezionata);
                    risposte.getStyleClass().add("risposte");
                    VBox vbox = new VBox(5, L_domanda, risposte);
                    vbox.setPrefWidth(800);
                    vbox.getStyleClass().add("domanda");
                    HBox contenuto = new HBox(5, L_numeroDomanda, vbox);
                    contenuto.getStyleClass().add("contenuto");
                    setGraphic(contenuto);
                }
            }
        });
    }

    @FXML
    private void avantiClicked() {
        Main.sessione.setQuizAttivo(null);
        cambiaSchermata("Leaderboard");
    }
}