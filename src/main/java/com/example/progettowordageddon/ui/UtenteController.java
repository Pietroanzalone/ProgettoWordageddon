package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Record;
import com.example.progettowordageddon.database.LeaderboardDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * @class UtenteController
 * @brief Il controller per la gestione della schermata "Utente".
 * @ingroup ui
 *
 * Gestisce il comportamento della schermata utente, inclusa
 * la visibilità del pannello di controllo e l'accesso alle
 * funzionalità disponibili.
 *
 * \image html Utente.png width=50%
 */
public class UtenteController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private Button B_pannelloDiControllo;

    @FXML
    private Label L_titolo;

    @FXML
    private GridPane GP_info;

    @FXML
    private Label L_punteggioMedio, L_ultimoQuiz, L_migliorPunteggio;
    /** \endcond */

    /**
     * @brief Inizializza la schermata utente.
     *
     * Imposta il titolo di benvenuto con il nome utente
     * e determina se mostrare il pulsante del pannello
     * di controllo in base ai privilegi dell'utente.
     */
    @Override
    public void initialize() {
        super.initialize();
        if (Main.sessione.getUtente().getUsername() == null) {
            Logger.error("Utente non inizializzato");
            L_titolo.setText("Benvenuto, NULL");
            B_pannelloDiControllo.setVisible(false);
            GP_info.setVisible(false);
        } else {
            L_titolo.setText("Benvenuto, " + Main.sessione.getUtente().getUsername());
            B_pannelloDiControllo.setVisible(Main.sessione.getUtente().isAdmin());
            impostaInfoBox();
        }
    }

    /**
     * @brief Visualizza le informazioni dei quiz dell'utente nella info box.
     *
     * Recupera tutti i record dell'utente corrente, calcola il miglior punteggio,
     * l'ultimo quiz svolto e la media dei punteggi, mostrandoli nella UI.
     * In caso di errore o assenza di record, la info box viene nascosta.
     */
    private void impostaInfoBox() {
        try {
            var lista = LeaderboardDAO.getPerUtente(Main.sessione.getUtente().getUsername());

            if (lista.isEmpty()) {
                GP_info.setVisible(false);
                return;
            }
            setMigliorPunteggio(lista);
            setUltimoQuiz(lista);
            setPunteggioMedio(lista);
        } catch (SQLException e) {
            GP_info.setVisible(false);
            mostraErrore(
                    "Impossibile caricare gli ultimi quiz",
                    "Errore: " + e.getMessage()
            ).showAndWait();
        }
    }

    /**
     * @brief Visualizza il punteggio medio dell'utente.
     *
     * Calcola il punteggio medio tra i record forniti e lo
     * visualizza nel campo corrispondente.
     *
     * @param lista Lista di record filtrata per l'utente corrente.
     */
    private void setPunteggioMedio(List<Record> lista) {
        assert(!lista.isEmpty());
        double media = lista.stream()
                .mapToInt(Record::punteggio)
                .average()
                .getAsDouble();
        L_punteggioMedio.setText(String.format("%.1f", media) + " / 10");
    }

    /**
     * @brief Visualizza l'ultimo quiz tentato dall'utente.
     *
     * Calcola il quiz più recente tra i record forniti e lo
     * visualizza nel campo corrispondente.
     *
     * @param lista Lista di record filtrata per l'utente corrente.
     */
    private void setUltimoQuiz(List<Record> lista) {
        assert(!lista.isEmpty());
        Record ultimo = lista.stream()
                .max(Comparator.comparing(Record::timestamp))
                .get();
        L_ultimoQuiz.setText(ultimo.toString());
    }

    /**
     * @brief Visualizza il miglior punteggio ottenuto dall'utente.
     *
     * Calcola il punteggio massimo tra i record forniti e lo
     * visualizza nel campo corrispondente.
     *
     * @param lista Lista di record filtrata per l'utente corrente.
     */
    private void setMigliorPunteggio(List<Record> lista) {
        assert(!lista.isEmpty());
        int migliorPunteggio = lista.stream()
                .max(Comparator.comparingInt(Record::punteggio))
                .get()
                .punteggio();
        L_migliorPunteggio.setText(migliorPunteggio + " / 10");
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Inizia Quiz".
     *
     * Cambia schermata verso la selezione del quiz.
     */
    @FXML
    private void iniziaQuizClicked() {
        Logger.log("Cliccato il pulsante: INIZIA QUIZ");
        cambiaSchermata("IniziaQuiz");
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Recupera Sessione".
     *
     * Apre una finestra di dialogo per selezionare un file di sessione salvato (.wordageddon).
     * Tenta di caricare la sessione selezionata. In caso di errore, mostra un messaggio
     * di errore appropriato. Se il file selezionato non appartiene all'utente corrente,
     * viene bloccato il caricamento.
     *
     * Dopo un caricamento corretto, la schermata corrente viene aggiornata in base allo stato
     * della sessione.
     */
    @FXML
    private void recuperaSessioneClicked() {
        Logger.log("Cliccato il pulsante: RECUPERA SESSIONE");

        var fileChooser = new FileChooser();
        fileChooser.setTitle("Recupera sessione");
        fileChooser.setInitialDirectory(new File("Sessioni"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sessione", "*.wordageddon"));
        File selezionato = fileChooser.showOpenDialog(L_titolo.getScene().getWindow());
        if (selezionato == null) {
            Logger.warn("Nessun file selezionato");
            return;
        }

        try {
            Main.sessione.caricaSessione(selezionato.getAbsolutePath());
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Impossibile caricare la sessione " + selezionato.getName());
            mostraErrore(
                "Impossibile caricare la sessione",
                "Questa sessione è danneggiata o corrisponde a una versione precedente dell'applicazione"
            ).showAndWait();
            return;
        } catch (IllegalArgumentException e) {
            Logger.error("Impossibile caricare la sessione " + selezionato.getName());
            mostraErrore(
                "Impossibile caricare la sessione",
                "Questa sessione è collegata a un altro utente"
            ).showAndWait();
            return;
        }
        cambiaSchermata(Main.sessione.getSchermata());
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Leaderboard".
     *
     * Cambia la schermata attiva mostrando la classifica generale dei punteggi
     * ottenuti dagli utenti nei quiz. La schermata fornisce una panoramica dei migliori risultati.
     */

    @FXML
    private void leaderboardClicked() {
        Logger.log("Cliccato il pulsante: LEADERBOARD");
        cambiaSchermata("Leaderboard");
    }

    /**
     * @brief Metodo eseguito al click del pulsante "Pannello di Controllo".
     *
     * Accessibile solo agli amministratori; porta alla schermata di gestione.
     */
    @FXML
    private void pannelloDiControlloClicked() {
        Logger.log("Cliccato il pulsante: PANNELLO DI CONTROLLO");
        cambiaSchermata("PannelloControllo");
    }

}