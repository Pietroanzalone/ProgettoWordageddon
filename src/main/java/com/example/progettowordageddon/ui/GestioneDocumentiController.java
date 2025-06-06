package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import com.example.progettowordageddon.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.*;

public class GestioneDocumentiController extends Controller {

    @FXML
    private TextField T_nome;

    @FXML
    private Label L_warn;

    @FXML
    private ChoiceBox<Lingua> C_lingua;

    @FXML
    private ChoiceBox<Difficolta> C_difficolta;

    @FXML
    private TextArea T_testo;

    @FXML
    private TableView<DocumentoTestuale> TV_documenti;

    @FXML
    private TableColumn<DocumentoTestuale, String> TC_nome;

    @FXML
    private TableColumn<DocumentoTestuale, String> TC_lingua;

    @FXML
    private TableColumn<DocumentoTestuale, String> TC_difficolta;

    @FXML
    private Button B_aggiungi;

    @FXML
    private Button B_elimina;

    @FXML
    private void aggiungiClicked() {}

    @FXML
    private void eliminaClicked() {}

    @Override
    public void initialize() {
        super.initialize();
        List<DocumentoTestuale> documenti;
        try {
            documenti = DocumentiTestualiDAO.getTutti();
        } catch (SQLException e) {
            documenti = new ArrayList<>();
        }

        caricamentoDati(documenti);
        setupColonne();
    }

    private void caricamentoDati(List<DocumentoTestuale> documenti) {
        TV_documenti.setItems(FXCollections.observableArrayList(documenti));
    }

    private void setupColonne() {
        TC_nome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
        TC_nome.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_lingua.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLingua().name()));
        TC_lingua.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_difficolta.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDifficolta().toString()));
        TC_difficolta.setCellFactory(TextFieldTableCell.forTableColumn());
    }

}