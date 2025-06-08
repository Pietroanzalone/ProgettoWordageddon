package com.example.progettowordageddon.ui;

import com.example.progettowordageddon.Main;
import com.example.progettowordageddon.database.UtentiDAO;
import com.example.progettowordageddon.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

/**
 * @class RegistratiController
 * @brief Il controller per la gestione della schermata "Registrati".
 *
 * Questa classe gestisce la registrazione di un nuovo utente,
 * visualizzando i messaggi di errore in caso di conflitti
 * o campi non validi, e abilita/disabilita il pulsante in
 * base allo stato dei campi.
 */
public class RegistratiController extends Controller {

    /** \cond DOXY_SKIP */
    @FXML
    private TextField T_username;

    @FXML
    private Label L_usernameWarn;

    @FXML
    private PasswordField P_password;

    @FXML
    private PasswordField P_confermaPassword;

    @FXML
    private Label L_passwordWarn;

    @FXML
    private Button B_registrati;
    /** \endcond */

    /**
     * @brief Metodo chiamato all'inizializzazione del controller.
     *
     * Inizializza la barra di navigazione e definisce le
     * logiche di validazione dei campi.
     */
    @Override
    public void initialize() {
        impostaNavbar();
        impostaIconaLogout();

        logicaUsername();
        logicaPassword();
        logicaPulsanteRegistrazione();
    }

    /**
     * @brief Metodo eseguito al click del pulsante "REGISTRATI".
     *
     * Crea un nuovo utente, lo inserisce nel database,
     * lo salva nella sessione e cambia schermata.
     *
     * @throws SQLException Se si verifica un errore nell'inserimento dell'utente.
     */
    @FXML
    public void registratiClicked() {
        Logger.log("Cliccato il pulsante: REGISTRATI");
        try {
            var utente = new Utente(
                T_username.getText(),
                P_password.getText(),
                false,
                false
            );
            utente.hash();
            UtentiDAO.aggiungi(utente);
            Main.sessione.setUtente(utente);
            cambiaSchermata("Utente");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("SQL Exception");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief Logica per la visualizzazione del messaggio di errore
     *        relativo al campo "Username".
     *
     * Mostra un avviso se lo username è già presente nel database.
     */
    private void logicaUsername() {
        L_usernameWarn.setVisible(false);
        T_username.textProperty().addListener((observable, oldValue, newValue) ->
                L_usernameWarn.setVisible(UtentiDAO.contiene(newValue))
        );
    }

    /**
     * @brief Logica per la visualizzazione del messaggio di errore
     *        relativo al campo "Password".
     *
     * Mostra un avviso se le due password inserite non coincidono.
     */
    private void logicaPassword() {
        L_passwordWarn.visibleProperty().bind(
                P_password.textProperty()
                        .isEqualTo(P_confermaPassword.textProperty())
                        .not()
        );
    }

    /**
     * @brief Logica di abilitazione del pulsante "Registrati".
     *
     * Il pulsante è abilitato solo quando tutti i campi sono
     * correttamente compilati e validi.
     */
    private void logicaPulsanteRegistrazione() {
        B_registrati.disableProperty().bind(
                T_username.textProperty().isEmpty()
                        .or(P_password.textProperty().isEmpty())
                        .or(L_usernameWarn.visibleProperty())
                        .or(L_passwordWarn.visibleProperty())
        );
    }

}
