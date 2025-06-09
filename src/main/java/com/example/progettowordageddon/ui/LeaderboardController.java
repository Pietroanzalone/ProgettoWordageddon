package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.database.LeaderboardDAO;
import com.example.progettowordageddon.model.*;
import com.example.progettowordageddon.model.Record;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * @class LeaderboardController
 * @brief Controller della schermata della classifica.
 *
 * Gestisce la visualizzazione dei punteggi salvati nel database, con possibilità
 * di filtrare per lingua e difficoltà.
 */
public class LeaderboardController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private ChoiceBox<Lingua> C_lingua;

    @FXML
    private ChoiceBox<Difficolta> C_difficolta;

    @FXML
    private TableView<Record> TV_classifica;

    private FilteredList<Record> filtrata;

    @FXML
    private TableColumn<Record, String> TC_username;

    @FXML
    private TableColumn<Record, String> TC_punteggio;

    @FXML
    private TableColumn<Record, String> TC_difficolta;

    @FXML
    private TableColumn<Record, String> TC_lingua;

    @FXML
    private TableColumn<Record, String> TC_data;

    @FXML
    private Label L_lingua;

    @FXML
    private Label L_difficolta;

    /** \endcond */

    /**
     * @brief Metodo di inizializzazione del controller.
     * Carica i dati, imposta la tabella e i filtri.
     */
    @Override
    public void initialize() {
        super.initialize();
        caricaDati();
        impostaStileRighe();
        impostaColonne();
        impostaChoiceBox();
        impostaFiltro();
    }

    /**
     * @brief Carica i dati dal database e li ordina per punteggio decrescente.
     * In caso di errore, la sessione viene resettata e viene mostrato un messaggio.
     */
    private void caricaDati() {
        List<Record> leaderboard;
        try {
            leaderboard = LeaderboardDAO.getTutti();
        } catch (SQLException e) {
            Logger.fatal("SQLException: " + e.getMessage());
            resettaSessione(
                    "Impossibile caricare la leaderboard dal database",
                    "La sessione verrà resettata"
            );
            return;
        }
        Collections.sort(leaderboard);
        filtrata = new FilteredList<>(FXCollections.observableArrayList(leaderboard), p -> true);
        TV_classifica.setItems(filtrata);
    }

    /**
     * @brief Applica uno stile speciale alle righe della tabella.
     * Evidenzia in grassetto la riga dell'utente corrente.
     */
    private void impostaStileRighe() {
        TV_classifica.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setStyle("");
                else if (Main.sessione.getUtente().getUsername().equals(item.getUsername()))
                    setStyle("-fx-font-weight: bold");
                else
                    setStyle("");
            }
        });
    }

    /**
     * @brief Imposta il contenuto e la formattazione delle colonne della tabella.
     * Collega ogni colonna ai relativi dati del record.
     */
    private void impostaColonne() {
        TC_username.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getUsername()));
        TC_username.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_punteggio.setCellValueFactory(cella -> new SimpleStringProperty(((Integer) cella.getValue().getPunteggio()).toString()));
        TC_punteggio.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_lingua.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getLingua().name()));
        TC_lingua.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_difficolta.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getDifficolta().name()));
        TC_difficolta.setCellFactory(TextFieldTableCell.forTableColumn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        TC_data.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getTimestamp().format(formatter)));
        TC_data.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * @brief Inizializza le ChoiceBox con i valori possibili di lingua e difficoltà.
     * Aggiunge anche un'opzione nulla (nessun filtro) e gestisce la visibilità delle etichette.
     */
    private void impostaChoiceBox() {
        List<Lingua> listaLingue = FXCollections.observableArrayList(Lingua.values());
        listaLingue.add(0, null);
        C_lingua.setItems(FXCollections.observableArrayList(listaLingue));
        L_lingua.visibleProperty().bind(C_lingua.valueProperty().isNull());

        List<Difficolta> listaDifficolta = FXCollections.observableArrayList(Difficolta.values());
        listaDifficolta.add(0, null);
        C_difficolta.setItems(FXCollections.observableArrayList(listaDifficolta));
        L_difficolta.visibleProperty().bind(C_difficolta.valueProperty().isNull());
    }

    /**
     * @brief Imposta i listener per applicare dinamicamente i filtri
     * quando cambia la selezione in uno dei due ChoiceBox.
     */
    private void impostaFiltro() {
        C_lingua.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applicaFiltro());
        C_difficolta.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applicaFiltro());
    }

    /**
     * @brief Applica i filtri selezionati (lingua e difficoltà) alla lista dei record.
     * Vengono mostrati solo i record che corrispondono ai filtri scelti.
     */
    private void applicaFiltro() {
        Lingua lingua = C_lingua.getValue();
        Difficolta difficolta = C_difficolta.getValue();

        filtrata.setPredicate(record -> {
            boolean linguaCorretta = (lingua == null) || lingua == record.getLingua();
            boolean difficoltaCorretta = (difficolta == null) || difficolta == record.getDifficolta();
            return linguaCorretta && difficoltaCorretta;
        });
    }
}
