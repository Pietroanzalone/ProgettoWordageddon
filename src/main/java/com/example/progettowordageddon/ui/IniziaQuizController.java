package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * @class IniziaQuizController
 * @brief Controller per la schermata di avvio del quiz.
 *
 * Gestisce la selezione della lingua e della difficoltà prima di iniziare un quiz.
 * Consente all'utente di configurare le impostazioni e poi avviare la sessione quiz.
 */
public class IniziaQuizController extends Controller {

    /** Lingua attualmente selezionata (default: ITALIANO). */
    private Lingua linguaScelta = Lingua.ITALIANO;

    /** Difficoltà attualmente selezionata (default: FACILE). */
    private Difficolta difficoltaScelta = Difficolta.FACILE;

    /** \cond DOXY_SKIP */
    @FXML
    private MenuButton B_difficolta;

    @FXML
    private MenuButton B_lingua;

    @FXML
    private MenuItem DIF_facile, DIF_media, DIF_difficile;

    @FXML
    private MenuItem LIN_italiano, LIN_inglese, LIN_spagnolo, LIN_francese;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione e imposta il comportamento
     * dei menu di scelta per difficoltà e lingua.
     *
     * @see #initDifficolta()
     * @see #initLingua()
     */
    @Override
    public void initialize() {
        super.initialize();
        initDifficolta();
        initLingua();
    }

    /**
     * @brief Inizializza la logica dei menu di scelta della difficoltà.
     *
     * Imposta le azioni associate alle tre opzioni (Facile, Media, Difficile)
     * e aggiorna lo stile del pulsante in base alla selezione.
     *
     * @see #styleDifficolta()
     */
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

    /**
     * @brief Inizializza la logica dei menu di scelta della lingua.
     *
     * Imposta le azioni associate alle lingue supportate
     * (italiano, inglese, francese, spagnolo) e aggiorna la grafica.
     *
     * @see #styleLingua()
     */
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

    /**
     * @brief Aggiorna lo stile visivo del pulsante "Difficoltà".
     *
     * Il colore cambia in base alla difficoltà selezionata:
     * - Facile: verde chiaro
     * - Media: giallo
     * - Difficile: rosso chiaro
     */
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

    /**
     * @brief Imposta l'icona del pulsante lingua in base alla lingua selezionata.
     *
     * L'immagine viene caricata dinamicamente e sostituisce il testo.
     */
    private void styleLingua() {
        B_lingua.setGraphic(caricaImmagine(linguaScelta.toString() + ".png", 130, 70));
        B_lingua.setText("");
        B_lingua.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * @brief Metodo chiamato al click sul pulsante "Inizia".
     *
     * Avvia la sessione del quiz con le impostazioni attuali
     * di lingua e difficoltà, e passa alla schermata "VediTesto".
     *
     * @see #cambiaSchermata(String)
     */
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
