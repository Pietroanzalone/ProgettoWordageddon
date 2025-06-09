package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.database.LeaderboardDAO;
import com.example.progettowordageddon.model.*;
import com.example.progettowordageddon.model.Record;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @class DomandaController
 * @brief Controller per la gestione dell'interfaccia utente del quiz a scelta multipla.
 *
 * Gestisce il caricamento e la visualizzazione delle domande, la selezione delle risposte
 * da parte dell'utente e il calcolo del punteggio finale.
 */
public class DomandaController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private Button B_avanti;

    @FXML
    private Label L_testoDomanda, L_numeroDomanda;

    @FXML
    private ToggleButton TB_risposta1, TB_risposta2, TB_risposta3, TB_risposta4;

    @FXML private ToggleGroup TG_risposte;
    /** \endcond */

    /** @brief Lista delle domande del quiz corrente. */
    private List<Domanda> domande;

    /** @brief Indice della domanda attualmente mostrata. */
    private int domandaAttuale;

    /**
     * @brief Metodo chiamato all'inizializzazione del controller.
     *
     */
    @Override
    public void initialize(){
        super.initialize();
        collegaPulsantiRisposta();
        bindPulsanteAvanti();
        caricaQuiz();
        mostraProssimaDomanda();
    }

    /**
     * @brief Collega i pulsanti di risposta al gruppo ToggleGroup.
     * Questo consente di avere una sola risposta selezionabile alla volta.
     */
    private void collegaPulsantiRisposta() {
        TB_risposta1.setToggleGroup(TG_risposte);
        TB_risposta2.setToggleGroup(TG_risposte);
        TB_risposta3.setToggleGroup(TG_risposte);
        TB_risposta4.setToggleGroup(TG_risposte);
        Logger.log("Pulsanti di risposta collegati");
    }

    /**
     * @brief Disabilita il pulsante "Avanti" finché non viene selezionata una risposta.
     */
    private void bindPulsanteAvanti() {
        B_avanti.disableProperty().bind(
                TG_risposte.selectedToggleProperty().isNull()
        );
        Logger.log("Bind pulsante AVANTI completato");
    }

    /**
     * @brief Carica le domande dal quiz attivo nella sessione corrente.
     */
    private void caricaQuiz() {
        domande = Main.sessione.getQuizAttivo().getDomande();
        domandaAttuale = 0;
        Logger.log("Domande caricate");
    }

    /**
     * @brief Mostra la prossima domanda del quiz nell'interfaccia utente.
     * Seleziona il testo della domanda e aggiorna i testi dei pulsanti di risposta.
     */
    private void mostraProssimaDomanda() {
        L_numeroDomanda.setText("Domanda " + (domandaAttuale + 1) + "/10");

        Toggle selected = TG_risposte.getSelectedToggle();
        if (selected != null)
            selected.setSelected(false);  // Deseleziona l'eventuale risposta precedente

        var domanda = domande.get(domandaAttuale);
        L_testoDomanda.setText(domanda.getTestoDomanda());
        TB_risposta1.setText(domanda.getRisposta1());
        TB_risposta2.setText(domanda.getRisposta2());
        TB_risposta3.setText(domanda.getRisposta3());
        TB_risposta4.setText(domanda.getRisposta4());
    }

    /**
     * @brief Gestisce il clic sul pulsante "Avanti".
     * Salva la risposta selezionata e mostra la prossima domanda o i risultati.
     */
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

    /**
     * @brief Salva l'indice della risposta selezionata dall'utente per la domanda attuale.
     */
    private void salvaRispostaUtente() {
        Toggle selezionato = TG_risposte.getSelectedToggle();
        int risposta = 0;  // default alla prima risposta

        if (selezionato == TB_risposta2)
            risposta = 1;
        if (selezionato == TB_risposta3)
            risposta = 2;
        if (selezionato == TB_risposta4)
            risposta = 3;

        domande.get(domandaAttuale).setSelezionata(risposta);
    }

    /**
     * @brief Mostra un messaggio di fine quiz e salva il punteggio nella leaderboard.
     * In caso di errore nel salvataggio, mostra un messaggio d’errore.
     */
    private void mostraRisultatiFinali() {
        int punteggio = Main.sessione.getQuizAttivo().getPunteggio();

        Alert risultato = new Alert(Alert.AlertType.INFORMATION);
        risultato.setTitle("Risultato");
        risultato.setHeaderText("Hai completato il quiz");
        risultato.setContentText("Risposte corrette: " + punteggio + "\n"
                + "Risposte errate: " + (10 - punteggio));
        risultato.showAndWait()
                .ifPresent(risposta -> {
                    try {
                        LeaderboardDAO.aggiungi(new Record(
                                Main.sessione.getUtente().getUsername(),
                                Main.sessione.getQuizAttivo()
                        ));
                    } catch (SQLException e) {
                        Logger.error("Impossibile salvare il quiz nella leaderboard");
                        mostraErrore(
                                "Impossibile salvare il quiz nella leaderboard",
                                "Errore: " + e.getMessage()
                        );
                    }
                    cambiaSchermata("RiepilogoQuiz");
                });
    }
}
