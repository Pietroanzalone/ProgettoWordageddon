package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.StopwordsDAO;
import com.example.progettowordageddon.model.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @class GestioneStopwordsController
 * @brief Il controller per la gestione della schermata "GestioneStopwords".
 *
 * Questa classe definisce la logica di interazione dell'interfaccia per
 * l'aggiunta, la visualizzazione e la rimozione delle stopword nel database.
 */
public class GestioneStopwordsController extends Controller {
    /** \cond DOXY_SKIP */
    @FXML
    private TextField T_word;

    @FXML
    private Button B_aggiungi;

    @FXML
    private Label L_warn;

    @FXML
    private Button B_elimina;

    @FXML
    private ListView<String> L_stopwords;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza tutti i componenti UI e imposta la logica di comportamento
     * per avvisi, pulsanti e lista delle stopword.
     */
    @Override
    public void initialize() {
        super.initialize();

        logicaLabelWarn();
        logicaPulsanteAggiungi();
        logicaListaStopwords();
        logicaPulsanteElimina();
    }

    /**
     * @brief Gestisce la visualizzazione del messaggio di avviso se la parola è già presente.
     *
     * Mostra un messaggio di avviso sotto il campo di testo se la parola digitata
     * è già presente nel database delle stopword.
     */
    private void logicaLabelWarn() {
        L_warn.setVisible(false);

        T_word.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isBlank()) {
                L_warn.setVisible(false);
                return;
            }

            if (StopwordsDAO.contiene(newValue.trim().toLowerCase())) {
                L_warn.setText(newValue + " è già presente nella lista");
                L_warn.setVisible(true);
            } else {
                L_warn.setVisible(false);
            }
        });
    }

    /**
     * @brief Gestisce l'abilitazione/disabilitazione del pulsante "Aggiungi".
     *
     * Il pulsante viene disabilitato se il campo è vuoto o se la parola è già presente.
     */
    private void logicaPulsanteAggiungi() {
        B_aggiungi.setDisable(true);

        T_word.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isBlank()) {
                B_aggiungi.setDisable(true);
                return;
            }

            B_aggiungi.setDisable(StopwordsDAO.contiene(newValue.trim().toLowerCase()));
        });
    }

    /**
     * @brief Carica la lista delle stopword dal database e la mostra ordinata.
     *
     * Le stopword vengono recuperate dal database e ordinate alfabeticamente.
     *
     * Nel caso in cui ci sia un errore nel recuperare le stopwords dal database,
     * mostra un messaggio di errore e resetta la sessione, tornando alla schermata "Home".
     */
    private void logicaListaStopwords() {
        List<String> stopwords;
        try {
            stopwords = StopwordsDAO.getTutti();
        } catch (SQLException e) {
            Logger.fatal("SQLException: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare le stopwords dal database");
            alert.setContentText("La sessione verrà resettata");
            alert.showAndWait().ifPresent(risposta -> cambiaSchermata("Home"));
            return;
        }

        Collections.sort(stopwords);
        L_stopwords.setItems(FXCollections.observableArrayList(stopwords));
    }

    /**
     * @brief Gestisce l'abilitazione del pulsante "Elimina" in base alla selezione.
     *
     * Il pulsante viene disabilitato se non è selezionata nessuna parola oppure
     * se la parola selezionata ha lunghezza pari a 1.
     */
    private void logicaPulsanteElimina() {
        B_elimina.disableProperty().bind(
            L_stopwords.getSelectionModel().selectedItemProperty().isNull()
                .or(Bindings.createBooleanBinding(() -> {
                    String selezionata = L_stopwords.getSelectionModel().getSelectedItem();
                    return selezionata != null && selezionata.length() == 1;
                }, L_stopwords.getSelectionModel().selectedItemProperty()))
        );
    }

    /**
     * @brief Metodo eseguito quando si clicca sul pulsante "Aggiungi".
     *
     * Aggiunge la nuova stopword nel database e aggiorna la lista visibile.
     *
     * @throws SQLException se si verifica un errore durante l'inserimento nel database.
     */
    @FXML
    private void aggiungiClicked() {
        try {
            StopwordsDAO.aggiungi(T_word.getText().trim().toLowerCase());
            B_aggiungi.setDisable(true);
            L_stopwords.getItems().add(T_word.getText().trim().toLowerCase());
            L_stopwords.getItems().setAll(L_stopwords.getItems().stream().sorted().toList());
            T_word.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief Metodo eseguito quando si clicca sul pulsante "Elimina".
     *
     * Elimina la stopword selezionata dalla lista e dal database.
     * Mostra una finestra di conferma prima di procedere.
     *
     * @throws SQLException se si verifica un errore durante l'eliminazione dal database.
     */
    @FXML
    private void eliminaClicked() {
        String selezionata = L_stopwords.getSelectionModel().getSelectedItem();
        if (selezionata == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nessuna selezione");
            alert.setHeaderText("Nessuna stopword selezionata");
            alert.setContentText("Seleziona una stopword dalla lista per eliminarla");
            alert.showAndWait();
            return;
        }

        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Vuoi davvero eliminare questa stopword?");
        conferma.setContentText("Stopword: " + selezionata);
        conferma.showAndWait()
                .filter(risposta -> risposta == ButtonType.OK)
                .ifPresent(risposta -> {
                    try {
                        StopwordsDAO.rimuovi(selezionata);
                        L_stopwords.getItems().remove(selezionata);
                        T_word.setText(selezionata);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Errore");
                        alert.setHeaderText("Impossibile eliminare la stopword " + selezionata);
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });
    }
}
