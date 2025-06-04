package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.StopwordsDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class GestioneStopwordsController extends Controller {

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

    @Override
    public void initialize() {
        super.initialize();

        logicaLabelWarn();
        logicaPulsanteAggiungi();
        logicaListaStopwords();
        logicaPulsanteElimina();
    }

    private void logicaLabelWarn() {
        L_warn.setVisible(false);

        T_word.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isBlank()) {
                L_warn.setVisible(false);
                return;
            }

            try {
                if (StopwordsDAO.contiene(newValue.trim().toLowerCase())) {
                    L_warn.setText(newValue + " è già presente nella lista");
                    L_warn.setVisible(true);
                } else
                    L_warn.setVisible(false);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("SQL Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    private void logicaPulsanteAggiungi() {
        B_aggiungi.setDisable(true);

        T_word.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isBlank()) {
                B_aggiungi.setDisable(true);
                return;
            }

            try {
                B_aggiungi.setDisable(StopwordsDAO.contiene(newValue.trim().toLowerCase()));
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("SQL Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    private void logicaListaStopwords() {
        L_stopwords.getItems().clear();
        try {
            L_stopwords.getItems().addAll(StopwordsDAO.getTutti().stream().sorted().toList());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void logicaPulsanteElimina() {
        B_elimina.disableProperty().bind(
            L_stopwords.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    private void aggiungiClicked() {
        try {
            StopwordsDAO.aggiungi(T_word.getText().trim().toLowerCase());
            B_aggiungi.setDisable(true);
            L_stopwords.getItems().add(T_word.getText().trim().toLowerCase());
            T_word.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("SQL Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

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