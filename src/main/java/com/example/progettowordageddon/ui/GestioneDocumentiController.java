package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import com.example.progettowordageddon.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

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
    private TableColumn<DocumentoTestuale, Difficolta> TC_difficolta;

    @FXML
    private Button B_aggiungi;

    @FXML
    private Button B_elimina;

    private boolean modalitaModifica;

    @FXML
    private void aggiungiClicked() {
        if (modalitaModifica) {
            DocumentoTestuale doc = TV_documenti.getSelectionModel().getSelectedItem();
            try {
                doc.setNome(T_nome.getText());
                doc.setLingua(C_lingua.getValue());
                doc.setDifficolta(C_difficolta.getValue());
                doc.setTesto(T_testo.getText());
                TV_documenti.refresh();
                TV_documenti.getSelectionModel().clearSelection();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Impossibile modificare il documento " + doc.getNome());
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            DocumentoTestuale doc = new DocumentoTestuale(
                T_nome.getText(),
                C_lingua.getValue(),
                C_difficolta.getValue(),
                T_testo.getText()
            );
            try {
                DocumentiTestualiDAO.aggiungi(doc);
                TV_documenti.getItems().add(doc);
                TV_documenti.getSelectionModel().clearSelection();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Impossibile aggiungere il documento " + doc.getNome());
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void eliminaClicked() {
        var selezionato = TV_documenti.getSelectionModel().getSelectedItem();
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Vuoi davvero eliminare questo documento?");
        conferma.setContentText("Documento: " + selezionato.getNome());
        conferma.showAndWait()
            .filter(risposta -> risposta == ButtonType.OK)
            .ifPresent(risposta -> {
                try {
                    DocumentiTestualiDAO.rimuovi(selezionato.getNome());
                    TV_documenti.getItems().remove(selezionato);
                    TV_documenti.getSelectionModel().clearSelection();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Impossibile eliminare il documento " + selezionato.getNome());
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
    }

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
        C_difficolta.setItems(FXCollections.observableArrayList(Difficolta.values()));
        C_lingua.setItems(FXCollections.observableArrayList(Lingua.values()));

        logicaPulsanteElimina();
        logicaModalitaModifica();
        logicaDeselezione();
        logicaAvvisoNome();
    }

    private void caricamentoDati(List<DocumentoTestuale> documenti) {
        Collections.sort(documenti);
        TV_documenti.setItems(FXCollections.observableArrayList(documenti));
    }

    private void setupColonne() {
        TC_nome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNome()));
        TC_nome.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_lingua.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLingua().name()));
        TC_lingua.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_difficolta.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDifficolta()));
        TC_difficolta.setCellFactory(this::difficoltaCellFactory);
    }

    private TableCell<DocumentoTestuale, Difficolta> difficoltaCellFactory(TableColumn<DocumentoTestuale, Difficolta> col) {
        return new TableCell<>() {
            @Override
            protected void updateItem(Difficolta difficolta, boolean empty) {
                super.updateItem(difficolta, empty);
                if (empty || difficolta == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(difficolta.toString());
                    setStyle(switch (difficolta) {
                        case FACILE -> "-fx-background-color: rgba(50,205,50,0.5);";
                        case MEDIA -> "-fx-background-color: rgba(223,223,93,0.5);";
                        case DIFFICILE -> "-fx-background-color: rgba(248,168,150,0.5);";
                    });
                }
            }
        };
    }

    private void logicaPulsanteElimina() {
        B_elimina.disableProperty().bind(
            TV_documenti.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private void logicaDeselezione() {
        TV_documenti.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            var target = event.getPickResult().getIntersectedNode();

            while (target != null && target != TV_documenti && !(target instanceof TableRow))
                target = target.getParent();

            if (target instanceof TableRow<?> riga)
                if (riga.isSelected()) {
                    TV_documenti.getSelectionModel().clearSelection();
                    event.consume();
                }
        });
    }

    private void logicaModalitaModifica() {
        modalitaModifica = false;

        TV_documenti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                modalitaModifica = true;
                B_aggiungi.setText("Modifica");
                T_nome.setText(newValue.getNome());
                C_lingua.setValue(newValue.getLingua());
                C_difficolta.setValue(newValue.getDifficolta());
                T_testo.setText(newValue.getTesto());
                L_warn.setVisible(false);
            } else {
                modalitaModifica = false;
                B_aggiungi.setText("Aggiungi");
                T_nome.setText("");
                C_lingua.setValue(null);
                C_difficolta.setValue(null);
                T_testo.setText("");
                L_warn.setVisible(false);
            }
        });

        B_aggiungi.disableProperty().bind(
            L_warn.visibleProperty()
            .or(T_nome.textProperty().isEmpty())
            .or(C_lingua.valueProperty().isNull())
            .or(C_difficolta.valueProperty().isNull())
            .or(T_testo.textProperty().isEmpty())
        );
    }

    private void logicaAvvisoNome() {
        T_nome.textProperty().addListener((observable, oldValue, newValue) -> {
            if (modalitaModifica) {
                try {
                    L_warn.setVisible(DocumentiTestualiDAO.contiene(newValue)
                        && !newValue.trim().equalsIgnoreCase(TV_documenti.getSelectionModel().getSelectedItem().getNome()));
                } catch (SQLException e) {
                    L_warn.setVisible(false);
                }
            } else {
                try {
                    L_warn.setVisible(DocumentiTestualiDAO.contiene(newValue));
                } catch (SQLException e) {
                    L_warn.setVisible(false);
                }
            }
        });
    }

}