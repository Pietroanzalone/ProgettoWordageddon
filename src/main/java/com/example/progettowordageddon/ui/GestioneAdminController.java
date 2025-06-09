package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.database.UtentiDAO;
import com.example.progettowordageddon.model.Logger;
import com.example.progettowordageddon.model.Utente;
import java.sql.SQLException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * @class GestioneAdminController
 * @brief Il controller per la gestione della schermata "GestioneAdmin".
 *
 * Questa classe definisce la logica di interazione dell'interfaccia per
 * l'aggiunta, la visualizzazione e la rimozione degli admin nel database.
 */
public class GestioneAdminController extends Controller {
    /** \cond DOXY_SKIP */
    @FXML
    private TextField T_username;

    @FXML
    private Button B_aggiungi;

    @FXML
    private Label L_warn;

    @FXML
    private Button B_elimina;

    @FXML
    private ListView<String> L_admins;
    /** \endcond */

    /**
     * @brief Metodo chiamato automaticamente all'inizializzazione del controller.
     *
     * Inizializza tutti i componenti UI e imposta la logica di comportamento
     * per avvisi, pulsanti e lista degli admin.
     */
    @Override
    public void initialize() {
        super.initialize();

        caricaAdmins();
        bindAvvisoUsernameInesistente();
        bindPulsanteAggiungi();
        bindPulsanteElimina();
    }

    /**
     * @brief Carica la lista degli admin dal database e la mostra ordinata.
     *
     * Gli admin vengono recuperati dal database e ordinati alfabeticamente per username.
     *
     * Nel caso in cui ci sia un errore nel recuperare gli admin dal database,
     * mostra un messaggio di errore e resetta la sessione, tornando alla schermata "Home".
     */
    private void caricaAdmins() {
        try {
            var admins = UtentiDAO.getTutti().stream()
                .filter(Utente::isAdmin)
                .map(Utente::getUsername)
                .sorted()
                .toList();

            L_admins.setItems(FXCollections.observableArrayList(admins));
        } catch (SQLException e) {
            Logger.fatal("SQLException: " + e.getMessage());
            resettaSessione(
                "Impossibile caricare gli admin dal database",
                "La sessione verrà resettata"
            );
        }
    }

    /**
     * @brief Gestisce la visualizzazione del messaggio di avviso.
     *
     * Mostra un messaggio di avviso sotto il campo di testo se
     * l'username non è presente nel database o è già admin.
     */
    private void bindAvvisoUsernameInesistente() {
        var usernameInesistente = Bindings.createBooleanBinding(() -> {
                String username = T_username.getText();
                return !UtentiDAO.contiene(username);
            },
            T_username.textProperty());

        var utenteGiaAdmin = Bindings.createBooleanBinding(() -> {
                String username = T_username.getText();
                boolean esiste = UtentiDAO.contiene(username);
                return esiste && UtentiDAO.get(username).isAdmin();
            },
            T_username.textProperty());

        L_warn.visibleProperty().bind(
            T_username.textProperty().isEmpty().not()
            .and(usernameInesistente)
            .or(utenteGiaAdmin)
        );
    }

    /**
     * @brief Gestisce l'abilitazione/disabilitazione del pulsante "Aggiungi".
     *
     * Il pulsante viene disabilitato se il campo è vuoto o se l'utente è già admin.
     */
    private void bindPulsanteAggiungi() {
        B_aggiungi.disableProperty().bind(
            L_warn.visibleProperty()
            .or(T_username.textProperty().isEmpty())
        );
    }

    /**
     * @brief Gestisce l'abilitazione del pulsante "Elimina" in base alla selezione.
     *
     * Il pulsante viene disabilitato se non è selezionato nessun admin.
     */
    private void bindPulsanteElimina() {
        B_elimina.disableProperty().bind(
            L_admins.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    /**
     * @brief Metodo eseguito quando si clicca sul pulsante "Aggiungi".
     *
     * Aggiunge il nuovo admin nel database e aggiorna la lista visibile.
     * Nel caso di errore SQL, mostra un messaggio di errore.
     */
    @FXML
    private void aggiungiClicked() {
        try {
            UtentiDAO.aggiornaAdmin(T_username.getText(), true);
            L_admins.getItems().add(T_username.getText());
            var nuovaLista = L_admins.getItems()
                .stream()
                .sorted()
                .toList();
            L_admins.getItems().setAll(nuovaLista);
            T_username.setText("");
        } catch (SQLException e) {
            mostraErrore(
                "Impossibile aggiungere l'admin",
                e.getMessage()
            ).showAndWait();
        }
    }

    /**
     * @brief Metodo eseguito quando si clicca sul pulsante "Elimina".
     *
     * Elimina l'admin selezionato dalla lista e dal database.
     * Mostra una finestra di conferma prima di procedere.
     * Se l'admin eliminato è l'utente attualmente loggato, aggiorna la sessione.
     * Nel caso di errore SQL, mostra un messaggio di errore.
     */
    @FXML
    private void eliminaClicked() {
        String selezionato = L_admins.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore(
                "Nessun admin selezionato",
                "Seleziona un admin dalla lista per eliminarlo"
            ).showAndWait();
            return;
        }
        chiediConferma(
            "Vuoi davvero eliminare questo admin?",
            "Admin: " + selezionato,
            () -> {
                try {
                    UtentiDAO.aggiornaAdmin(selezionato, false);
                    L_admins.getItems().remove(selezionato);
                    T_username.setText(selezionato);
                    if (Main.sessione.getUtente().getUsername().equals(selezionato))
                        Main.sessione.setUtente(UtentiDAO.get(selezionato));
                } catch (SQLException e) {
                    mostraErrore(
                        "Impossibile eliminare l'admin " + selezionato,
                        e.getMessage()
                    ).showAndWait();
                }
            }
        );
    }
}
