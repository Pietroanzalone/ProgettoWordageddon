package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Iterator;
import java.util.List;


public class DomandaController extends Controller {

    @FXML
    private Button ButtonNext;

    @FXML
    private Label TestoDomanda, NumeroDomanda;

    @FXML
    private ToggleButton Risposta1, Risposta2, Risposta3, Risposta4;

    @FXML
    private ToggleGroup TgRisposte;

    private final Quiz quiz= Main.sessione.getQuizAttivo();

    private final List<Domanda> domande= Main.sessione.getQuizAttivo().getDomande();

    private final Iterator<Domanda> iterator = domande.iterator();

    private Domanda domandaCorrente = Main.sessione.getQuizAttivo().getDomande().get(0);

    private int counter = 1;

    @Override
    public void initialize(){
        super.initialize();

        Risposta1.setToggleGroup(TgRisposte);
        Risposta2.setToggleGroup(TgRisposte);
        Risposta3.setToggleGroup(TgRisposte);
        Risposta4.setToggleGroup(TgRisposte);

        TgRisposte.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ButtonNext.setDisable(false);
            } else {
                ButtonNext.setDisable(true);
            }
        });
        mostraProssimaDomanda();

    }

    private void mostraProssimaDomanda(){
        if(iterator.hasNext()){
            Domanda d =iterator.next();

            NumeroDomanda.setText("Domanda N°" + counter);
            TestoDomanda.setText(d.getTestoDomanda());
            Risposta1.setText(d.getRisposta1());
            Risposta2.setText(d.getRisposta2());
            Risposta3.setText(d.getRisposta3());
            Risposta4.setText(d.getRisposta4());

            TgRisposte.selectToggle(null);
            ButtonNext.setDisable(true);

            counter++;
        } else {
            mostraRisultatiFinali();
        }

        }

        @FXML
        private void ButtonNextCliccato(){
        salvaRispostaUtente();
        mostraProssimaDomanda();
        }


    private void salvaRispostaUtente() {
        Toggle selectedToggle = TgRisposte.getSelectedToggle();
        if (selectedToggle != null && domandaCorrente != null) {
            int rispostaSelezionata = -1;

            if (selectedToggle == Risposta1) {
                rispostaSelezionata = 0;
            } else if (selectedToggle == Risposta2) {
                rispostaSelezionata = 1;
            } else if (selectedToggle == Risposta3) {
                rispostaSelezionata = 2;
            } else if (selectedToggle == Risposta4) {
                rispostaSelezionata = 3;
            }

            if (rispostaSelezionata != -1) {
                domandaCorrente.setSelezionata(rispostaSelezionata);
            }
        }
    }

    private void mostraRisultatiFinali() {
        int punteggio = quiz.getPunteggio();
        int totale = domande.size();
        int sbagliate = totale - punteggio;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Risultato del Quiz");
        alert.setHeaderText("Hai completato il quiz!");
        alert.setContentText("Risposte corrette: " + punteggio + " su " + totale +
                "\nRisposte sbagliate: " + sbagliate);
        alert.showAndWait();

        ButtonNext.setDisable(true);
    }



}

