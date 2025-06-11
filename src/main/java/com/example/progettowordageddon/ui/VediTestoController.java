package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @class VediTestoController
 * @brief Il controller per la gestione della schermata "VediTesto".
 * @ingroup ui
 *
 * Questa classe gestisce la visualizzazione del testo del primo e,
 * se necessario, del secondo documento, con un timer che limita il
 * tempo di lettura in base alla difficoltà del quiz.
 *
 * \image html VediTesto.png width=80%
 */
public class VediTestoController extends Controller {

    /** @brief Flag che indica se è stato mostrato il secondo documento. */
    private boolean secondoDocumento = false;

    /** @brief Quiz attivo della sessione corrente. */
    private Quiz quizAttivo;

    /** @brief Timer che gestisce il conto alla rovescia per la lettura del testo. */
    private Timeline timer;

    /** \cond DOXY_SKIP */
    @FXML
    private Label L_titolo;

    @FXML
    private TextArea T_testo;

    @FXML
    private Label L_timer;
    /** \endcond */

    /**
     * @brief Inizializza il controller.
     *
     * Recupera il quiz attivo dalla sessione, configura il campo di testo come non editabile,
     * imposta il timer in base alla difficoltà del quiz e mostra il primo documento da leggere.
     */
    @Override
    public void initialize() {
        super.initialize();
        quizAttivo = Main.sessione.getQuizAttivo();
        T_testo.setEditable(false);
        gestisciDifficolta(quizAttivo.getDifficolta());
        mostraTesto(quizAttivo.getPrimoDocumento());
    }

    /**
     * @brief Imposta il timer in base alla difficoltà del quiz.
     * @param difficolta Difficoltà del quiz.
     */
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

    /**
     * @brief Avvia il timer con il numero di secondi specificato.
     * Aggiorna il label con il tempo rimanente ogni secondo.
     * @param secondi Durata del timer in secondi.
     */
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
                passaAvanti();
                timer.stop();
            }
        }));
        timer.setCycleCount(secondi + 3);
        timer.play();
    }

    /**
     * @brief Metodo chiamato al click del pulsante "Pronto".
     * Interrompe il timer e passa alla schermata successiva.
     */
    @FXML
    private void prontoClicked() {
        Logger.log("Cliccato il pulsante: PRONTO");
        if (timer != null) timer.stop();
        passaAvanti();
    }

    /**
     * @brief Gestisce il passaggio alla schermata successiva.
     *
     * Per la difficoltà DIFFICILE mostra prima il secondo documento, poi passa alle domande.
     * Per le altre difficoltà passa direttamente alla schermata delle domande del quiz.
     */
    private void passaAvanti() {
        if (quizAttivo.getDifficolta() == Difficolta.DIFFICILE) {
            if (!secondoDocumento) {
                gestisciDifficolta(quizAttivo.getDifficolta());
                mostraTesto(quizAttivo.getSecondoDocumento());
                secondoDocumento = true;
            } else
                cambiaSchermata("Domanda");
        } else
            cambiaSchermata("Domanda");
    }

    /**
     * @brief Mostra il testo del documento testuale nella GUI.
     * @param documento Documento da mostrare.
     */
    private void mostraTesto(DocumentoTestuale documento) {
        L_titolo.setText(documento.getNome());
        try {
            T_testo.setText(documento.getTesto());
        } catch (SQLException e) {
            Logger.fatal("Impossibile mostrare il testo: " + e.getMessage());
            resettaSessione(
                    "Impossibile caricare il testo",
                    "La versione verrà resettata"
            );
        }
    }

}