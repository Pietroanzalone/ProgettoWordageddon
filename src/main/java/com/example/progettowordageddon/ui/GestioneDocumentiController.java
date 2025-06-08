package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.database.DocumentiTestualiDAO;
import com.example.progettowordageddon.model.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

/**
 * @class GestioneDocumentiController
 * @brief Il controller per la gestione della schermata "GestioneDocumenti".
 *
 * Questa classe definisce la logica di interazione dell'interfaccia per
 * l'aggiunta, la visualizzazione, la modifica e la rimozione dei
 * documenti testuali nel database.
 */
public class GestioneDocumentiController extends Controller {

    /** @brief Flag che indica se si sta modificando un documento esistente
     *
     *  Se `true`, allora si sta modificando un documento già presente
     *  nel database, altrimenti si sta aggiungendo un nuovo documento.
     *
     * @default{false}
     */
    private BooleanProperty modalitaModifica;

    /** \cond DOXY_SKIP */
    @FXML
    private TextField T_nome;

    @FXML
    private Label L_warn, L_lingua, L_difficolta;

    @FXML
    private ChoiceBox<Lingua> C_lingua;

    @FXML
    private ChoiceBox<Difficolta> C_difficolta;

    @FXML
    private TextArea T_testo;

    @FXML
    private TableView<DocumentoTestuale> TV_documenti;

    @FXML
    private TableColumn<DocumentoTestuale, String> TC_nome, TC_lingua;

    @FXML
    private TableColumn<DocumentoTestuale, Difficolta> TC_difficolta;

    @FXML
    private Button B_aggiungi, B_elimina;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza tutti i componenti UI e imposta la logica di comportamento
     * per avvisi, pulsanti e campi di testo e tabella dei documenti.
     */
    @Override
    public void initialize() {
        super.initialize();

        caricaDati();
        impostaColonne();
        impostaChoiceBox();
        logicaSelezioneDeselezioneRiga();
        logicaModalitaModifica();
        bindPulsanteElimina();
        bindPulsanteAggiungi();
        bindWarnNome();
    }

    /**
     * @brief Carica la lista dei documenti dal database e la inserisce nella tabella.
     *
     * I documenti vengono recuperati dal database e ordinati in base a nome, lingua e difficoltà.
     *
     * Nel caso in cui ci sia un errore nel recuperare i documenti dal database,
     * mostra un messaggio di errore e resetta la sessione, tornando alla schermata "Home".
     */
    private void caricaDati() {
        List<DocumentoTestuale> documenti;
        try {
            documenti = DocumentiTestualiDAO.getTutti();
        } catch (SQLException e) {
            Logger.fatal("SQLException: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare i documenti dal database");
            alert.setContentText("La sessione verrà resettata");
            alert.showAndWait().ifPresent(risposta -> cambiaSchermata("Home"));
            return;
        }

        Collections.sort(documenti);
        TV_documenti.setItems(FXCollections.observableArrayList(documenti));
    }

    /**
     * @brief Imposta le celle delle colonne della tabella
     *        per mostrare correttamente i dati dei documenti.
     *
     * Collega le proprietà dei documenti alle rispettive colonne
     * della tabella e applica le rispettive formattazioni.
     */
    private void impostaColonne() {
        TC_nome.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getNome()));
        TC_nome.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_lingua.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getLingua().name()));
        TC_lingua.setCellFactory(TextFieldTableCell.forTableColumn());

        TC_difficolta.setCellValueFactory(cella -> new SimpleObjectProperty<>(cella.getValue().getDifficolta()));
        TC_difficolta.setCellFactory(this::difficoltaCellFactory);
    }

    /**
     * @brief Metodo di utility che crea una cella personalizzata
     *        per la colonna della difficoltà nella tabella.
     *
     * La cella visualizza la difficoltà con un colore di
     * sfondo diverso in base al livello.
     *
     * @param col La colonna della tabella per cui creare la cella.
     * @return Una nuova TableCell con stile personalizzato.
     */
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
                        case FACILE    -> "-fx-background-color: rgba( 50,205, 50,0.5);";
                        case MEDIA     -> "-fx-background-color: rgba(223,223, 93,0.5);";
                        case DIFFICILE -> "-fx-background-color: rgba(248,168,150,0.5);";
                    });
                }
            }
        };
    }

    /**
     * @brief Imposta le opzioni delle ChoiceBox per lingua e difficoltà.
     *
     * Inoltre, rende visibili i placeholder se nessuna opzione è selezionata.
     */
    private void impostaChoiceBox() {
        C_lingua.setItems(FXCollections.observableArrayList(Lingua.values()));
        L_lingua.visibleProperty().bind(C_lingua.valueProperty().isNull());
        C_difficolta.setItems(FXCollections.observableArrayList(Difficolta.values()));
        L_difficolta.visibleProperty().bind(C_difficolta.valueProperty().isNull());
    }

    /**
     * @brief Definisce la logica di abilitazione del pulsante "Elimina"
     *        in base a modalitaModifica.
     *
     * Il pulsante è abilitato solo se è selezionato un documento esistente.
     */
    private void bindPulsanteElimina() {
        B_elimina.disableProperty().bind(
            modalitaModifica.not()
        );
    }

    /**
     * @brief Definisce la logica di abilitazione del pulsante "Aggiungi"
     *        in base alla validità dei campi.
     *
     * Disabilita il pulsante se i campi degli attributi del documento
     * sono vuoti o se c'è un conflitto di nome nel database.
     */
    private void bindPulsanteAggiungi() {
        var nomeNonValido = Bindings.createBooleanBinding(
            () -> T_nome.getText().isBlank(), T_nome.textProperty()
        );
        var testoNonValido = Bindings.createBooleanBinding(
            () -> T_testo.getText().isBlank(), T_testo.textProperty()
        );

        B_aggiungi.disableProperty().bind(
            L_warn.visibleProperty()
            .or(nomeNonValido)
            .or(C_lingua.valueProperty().isNull())
            .or(C_difficolta.valueProperty().isNull())
            .or(testoNonValido)
        );
    }

    /**
     * @brief Definisce la logica di visibilità dell'avviso sul nome duplicato.
     *
     * Mostra un'etichetta di avvertimento se il nome inserito è già presente
     * nel database.
     */
    private void bindWarnNome() {
        var nomeGiaPresente = Bindings.createBooleanBinding(
            () -> DocumentiTestualiDAO.contiene(T_nome.getText()), T_nome.textProperty()
        );
        var nomeDiversoDaPrecedente = Bindings.createBooleanBinding(
            () -> {
                var nuovoNome = T_nome.getText().trim();
                var rigaSelezionata = TV_documenti.getSelectionModel().getSelectedItem();
                if (rigaSelezionata == null)
                    return true;
                return !nuovoNome.equals(rigaSelezionata.getNome());
            },
            T_nome.textProperty(),
            TV_documenti.getSelectionModel().selectedItemProperty()
        );

        L_warn.visibleProperty().bind(
            modalitaModifica.not()
            .and(nomeGiaPresente)
            .or(
                modalitaModifica
                .and(nomeGiaPresente)
                .and(nomeDiversoDaPrecedente)
            )
        );
    }

    /**
     * @brief Gestisce la selezione e deselezione delle righe della tabella.
     *
     * Se si clicca su una riga già selezionata, viene deselezionata.
     * In caso contrario, entra in modalità modifica.
     */
    private void logicaSelezioneDeselezioneRiga() {
        TV_documenti.setRowFactory(tabella -> {
            TableRow<DocumentoTestuale> riga = new TableRow<>();
            riga.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                modalitaModifica.set(false);
                if (!riga.isEmpty()) {
                    if (!riga.isSelected())
                        Platform.runLater(() -> modalitaModifica.set(TV_documenti.getSelectionModel().getSelectedItem() != null));
                    else {
                        TV_documenti.getSelectionModel().clearSelection();
                        event.consume();
                    }
                }
            });
            return riga;
        });
    }

    /**
     * @brief Definisce il comportamento dei campi in modalità modifica.
     *
     * Aggiorna i campi del form e il testo del pulsante di aggiunta in
     * base alla modalità corrente.
     */
    private void logicaModalitaModifica() {
        modalitaModifica = new SimpleBooleanProperty(false);

        modalitaModifica.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Logger.log("Modalità modifica: TRUE");
                B_aggiungi.setText("Modifica");
                impostaCampi(TV_documenti.getSelectionModel().getSelectedItem());
            } else {
                Logger.log("Modalità modifica: FALSE");
                B_aggiungi.setText("Aggiungi");
                impostaCampi(null);
            }
        });
    }

    /**
     * @brief Metodo di utility per impostare i valori dei campi del form.
     *
     * Imposta i valori dei campi del form in base a un DocumentoTestuale
     * fornito in ingresso.
     *
     * @param doc Documento di cui prendere i valori.
     *            Se null, svuota i campi del form.
     */
    private void impostaCampi(DocumentoTestuale doc) {
        if (doc == null) {
            T_nome.setText("");
            C_lingua.setValue(null);
            C_difficolta.setValue(null);
            T_testo.setText("");
            return;
        }

        T_nome.setText(doc.getNome());
        C_lingua.setValue(doc.getLingua());
        C_difficolta.setValue(doc.getDifficolta());
        try {
            T_testo.setText(doc.getTesto());
        } catch (SQLException e) {
            Logger.fatal("Impossibile mostrare il testo: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare il testo");
            alert.setContentText("La sessione verrà resettata");
            alert.showAndWait()
                .ifPresent(risposta -> cambiaSchermata("Home"));
        }
    }

    /**
     * @brief Gestisce il comportamento del pulsante "Aggiungi"/"Modifica".
     *
     * Chiama il metodo appropriato in base alla modalità attuale tra
     * {@link modificaDocumento} e {@link aggiungiDocumento}.
     */
    @FXML
    private void aggiungiClicked() {
        if (modalitaModifica.get())
            modificaDocumento();
        else
            aggiungiDocumento();
    }

    /**
     * @brief Modifica il documento selezionato con i nuovi valori
     *        dai campi del form.
     *
     * Aggiorna i dati del documento nel database e ricarica la tabella.
     */
    private void modificaDocumento() {
        Logger.log("Cliccato il pulsante: MODIFICA");
        var doc = TV_documenti.getSelectionModel().getSelectedItem();
        try {
            doc.setNome(T_nome.getText());
            doc.setLingua(C_lingua.getValue());
            doc.setDifficolta(C_difficolta.getValue());
            doc.setTesto(T_testo.getText());
            TV_documenti.getSelectionModel().clearSelection();
            modalitaModifica.set(false);
        } catch (SQLException e) {
            Logger.error("SQLException: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile modificare il documento");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        TV_documenti.refresh();
    }

    /**
     * @brief Aggiunge un nuovo documento con i valori inseriti nei
     *        campi del form.
     *
     * Inserisce il nuovo documento nel database e ricarica la tabella.
     */
    private void aggiungiDocumento() {
        Logger.log("Cliccato il pulsante: AGGIUNGI");
        var doc = new DocumentoTestuale(
            T_nome.getText(),
            C_lingua.getValue(),
            C_difficolta.getValue(),
            T_testo.getText()
        );
        try {
            DocumentiTestualiDAO.aggiungi(doc);
            TV_documenti.getItems().add(doc);
            impostaCampi(null);
        } catch (SQLException e) {
            Logger.error("SQLException: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile aggiungere il documento");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        TV_documenti.refresh();
    }

    /**
     * @brief Gestisce il comportamento del pulsante "Elimina".
     *
     * Chiede conferma da parte dell'utente. In caso positivo, elimina
     * il documento selezionato dal database e lo rimuove dalla tabella.
     */
    @FXML
    private void eliminaClicked() {
        Logger.log("Cliccato il pulsante: ELIMINA");
        var selezionato = TV_documenti.getSelectionModel().getSelectedItem();
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Vuoi davvero eliminare questo documento?");
        conferma.setContentText("Documento: " + selezionato);
        conferma.showAndWait()
            .filter(risposta -> risposta == ButtonType.OK)
            .ifPresent(risposta -> {
                try {
                    DocumentiTestualiDAO.rimuovi(selezionato.getNome());
                    TV_documenti.getItems().remove(selezionato);
                    TV_documenti.getSelectionModel().clearSelection();
                } catch (SQLException e) {
                    Logger.error("SQLException: " + e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Impossibile eliminare il documento");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
    }

}