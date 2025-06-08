package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;


public class DomandaController extends Controller {

    @FXML
    private Button B_avanti;

    @FXML
    private Label L_testoDomanda, L_numeroDomanda;

    @FXML
    private ToggleButton TB_risposta1, TB_risposta2, TB_risposta3, TB_risposta4;

    @FXML
    private ToggleGroup TG_risposte;

    private List<Domanda> domande;

    private int domandaAttuale;

    @Override
    public void initialize(){
        super.initialize();

        collegaPulsantiRisposta();
        bindPulsanteAvanti();
        caricaQuiz();
        mostraProssimaDomanda();
    }

    private void collegaPulsantiRisposta() {
        TB_risposta1.setToggleGroup(TG_risposte);
        TB_risposta2.setToggleGroup(TG_risposte);
        TB_risposta3.setToggleGroup(TG_risposte);
        TB_risposta4.setToggleGroup(TG_risposte);
        Logger.log("Pulsanti di risposta collegati");
    }

    private void bindPulsanteAvanti() {
        B_avanti.disableProperty().bind(
            TG_risposte.selectedToggleProperty().isNull()
        );
        Logger.log("Bind pulsante AVANTI completato");
    }

    private void caricaQuiz() {
        domande = Main.sessione.getQuizAttivo().getDomande();
        domandaAttuale = 0;
        Logger.log("Domande caricate");
    }

    private void mostraProssimaDomanda() {
        L_numeroDomanda.setText("Domanda " + (domandaAttuale + 1) + "/10");

        Toggle selected = TG_risposte.getSelectedToggle();
        if (selected != null)
            selected.setSelected(false);

        var domanda = domande.get(domandaAttuale);
        L_testoDomanda.setText(domanda.getTestoDomanda());
        TB_risposta1.setText(domanda.getRisposta1());
        TB_risposta2.setText(domanda.getRisposta2());
        TB_risposta3.setText(domanda.getRisposta3());
        TB_risposta4.setText(domanda.getRisposta4());
    }

    @FXML
    private void avantiClicked() {
        Logger.log("Cliccato il pulsante: AVANTI");
        salvaRispostaUtente();
        domandaAttuale++;
        if (domandaAttuale < 10)
            mostraProssimaDomanda();
        else
            mostraRisultatiFinali();
    }

    private void salvaRispostaUtente() {
        Toggle selezionato = TG_risposte.getSelectedToggle();
        int risposta = 0;
        if (selezionato == TB_risposta2)
            risposta = 1;
        if (selezionato == TB_risposta3)
            risposta = 2;
        if (selezionato == TB_risposta4)
            risposta = 3;

        domande.get(domandaAttuale).setSelezionata(risposta);
    }

    private void mostraRisultatiFinali() {
        int punteggio = Main.sessione.getQuizAttivo().getPunteggio();
        Alert risultato = new Alert(Alert.AlertType.INFORMATION);
        risultato.setTitle("Risultato");
        risultato.setHeaderText("Hai completato il quiz");
        risultato.setContentText("Risposte corrette: " + punteggio + "\n"
                               + "Risposte errate: " + (10 - punteggio));
        risultato.showAndWait()
            .ifPresent(risposta -> {
//                QuizDAO.aggiungi(Main.sessione.getQuizAttivo());
                Main.sessione.setQuizAttivo(null);
                cambiaSchermata("Home");
            });
    }

}